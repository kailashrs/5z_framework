package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedRotateDrawable
  extends DrawableWrapper
  implements Animatable
{
  private float mCurrentDegrees;
  private float mIncrement;
  private final Runnable mNextFrame = new Runnable()
  {
    public void run()
    {
      AnimatedRotateDrawable.access$216(AnimatedRotateDrawable.this, mIncrement);
      if (mCurrentDegrees > 360.0F - mIncrement) {
        AnimatedRotateDrawable.access$202(AnimatedRotateDrawable.this, 0.0F);
      }
      invalidateSelf();
      AnimatedRotateDrawable.this.nextFrame();
    }
  };
  private boolean mRunning;
  private AnimatedRotateState mState;
  
  public AnimatedRotateDrawable()
  {
    this(new AnimatedRotateState(null, null), null);
  }
  
  private AnimatedRotateDrawable(AnimatedRotateState paramAnimatedRotateState, Resources paramResources)
  {
    super(paramAnimatedRotateState, paramResources);
    mState = paramAnimatedRotateState;
    updateLocalState();
  }
  
  private void nextFrame()
  {
    unscheduleSelf(mNextFrame);
    scheduleSelf(mNextFrame, SystemClock.uptimeMillis() + mState.mFrameDuration);
  }
  
  private void updateLocalState()
  {
    mIncrement = (360.0F / mState.mFramesCount);
    Drawable localDrawable = getDrawable();
    if (localDrawable != null)
    {
      localDrawable.setFilterBitmap(true);
      if ((localDrawable instanceof BitmapDrawable)) {
        ((BitmapDrawable)localDrawable).setAntiAlias(true);
      }
    }
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    AnimatedRotateState localAnimatedRotateState = mState;
    if (localAnimatedRotateState == null) {
      return;
    }
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    AnimatedRotateState.access$002(localAnimatedRotateState, paramTypedArray.extractThemeAttrs());
    boolean bool1 = paramTypedArray.hasValue(2);
    boolean bool2 = false;
    TypedValue localTypedValue;
    float f;
    if (bool1)
    {
      localTypedValue = paramTypedArray.peekValue(2);
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
    if (paramTypedArray.hasValue(3))
    {
      localTypedValue = paramTypedArray.peekValue(3);
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
    setFramesCount(paramTypedArray.getInt(5, mFramesCount));
    setFramesDuration(paramTypedArray.getInt(4, mFrameDuration));
  }
  
  private void verifyRequiredAttributes(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[1] == 0)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramTypedArray.getPositionDescription());
      localStringBuilder.append(": <animated-rotate> tag requires a 'drawable' attribute or child tag defining a drawable");
      throw new XmlPullParserException(localStringBuilder.toString());
    }
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 187	android/graphics/drawable/DrawableWrapper:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 37	android/graphics/drawable/AnimatedRotateDrawable:mState	Landroid/graphics/drawable/AnimatedRotateDrawable$AnimatedRotateState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_2
    //   16: invokestatic 162	android/graphics/drawable/AnimatedRotateDrawable$AnimatedRotateState:access$000	(Landroid/graphics/drawable/AnimatedRotateDrawable$AnimatedRotateState;)[I
    //   19: ifnull +50 -> 69
    //   22: aload_1
    //   23: aload_2
    //   24: invokestatic 162	android/graphics/drawable/AnimatedRotateDrawable$AnimatedRotateState:access$000	(Landroid/graphics/drawable/AnimatedRotateDrawable$AnimatedRotateState;)[I
    //   27: getstatic 193	com/android/internal/R$styleable:AnimatedRotateDrawable	[I
    //   30: invokevirtual 199	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   33: astore_1
    //   34: aload_0
    //   35: aload_1
    //   36: invokespecial 201	android/graphics/drawable/AnimatedRotateDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;)V
    //   39: aload_0
    //   40: aload_1
    //   41: invokespecial 203	android/graphics/drawable/AnimatedRotateDrawable:verifyRequiredAttributes	(Landroid/content/res/TypedArray;)V
    //   44: aload_1
    //   45: invokevirtual 206	android/content/res/TypedArray:recycle	()V
    //   48: goto +21 -> 69
    //   51: astore_2
    //   52: goto +11 -> 63
    //   55: astore_2
    //   56: aload_2
    //   57: invokestatic 210	android/graphics/drawable/AnimatedRotateDrawable:rethrowAsRuntimeException	(Ljava/lang/Exception;)V
    //   60: goto -16 -> 44
    //   63: aload_1
    //   64: invokevirtual 206	android/content/res/TypedArray:recycle	()V
    //   67: aload_2
    //   68: athrow
    //   69: aload_0
    //   70: invokespecial 40	android/graphics/drawable/AnimatedRotateDrawable:updateLocalState	()V
    //   73: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	74	0	this	AnimatedRotateDrawable
    //   0	74	1	paramTheme	Resources.Theme
    //   9	15	2	localAnimatedRotateState	AnimatedRotateState
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
    AnimatedRotateState localAnimatedRotateState = mState;
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
    j = paramCanvas.save();
    paramCanvas.rotate(mCurrentDegrees, left + f1, top + f2);
    localDrawable.draw(paramCanvas);
    paramCanvas.restoreToCount(j);
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AnimatedRotateDrawable);
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    updateStateFromTypedArray(localTypedArray);
    verifyRequiredAttributes(localTypedArray);
    localTypedArray.recycle();
    updateLocalState();
  }
  
  public boolean isRunning()
  {
    return mRunning;
  }
  
  DrawableWrapper.DrawableWrapperState mutateConstantState()
  {
    mState = new AnimatedRotateState(mState, null);
    return mState;
  }
  
  public void setFramesCount(int paramInt)
  {
    mState.mFramesCount = paramInt;
    mIncrement = (360.0F / mState.mFramesCount);
  }
  
  public void setFramesDuration(int paramInt)
  {
    mState.mFrameDuration = paramInt;
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool = super.setVisible(paramBoolean1, paramBoolean2);
    if (paramBoolean1)
    {
      if ((bool) || (paramBoolean2))
      {
        mCurrentDegrees = 0.0F;
        nextFrame();
      }
    }
    else {
      unscheduleSelf(mNextFrame);
    }
    return bool;
  }
  
  public void start()
  {
    if (!mRunning)
    {
      mRunning = true;
      nextFrame();
    }
  }
  
  public void stop()
  {
    mRunning = false;
    unscheduleSelf(mNextFrame);
  }
  
  static final class AnimatedRotateState
    extends DrawableWrapper.DrawableWrapperState
  {
    int mFrameDuration = 150;
    int mFramesCount = 12;
    float mPivotX = 0.0F;
    boolean mPivotXRel = false;
    float mPivotY = 0.0F;
    boolean mPivotYRel = false;
    private int[] mThemeAttrs;
    
    public AnimatedRotateState(AnimatedRotateState paramAnimatedRotateState, Resources paramResources)
    {
      super(paramResources);
      if (paramAnimatedRotateState != null)
      {
        mPivotXRel = mPivotXRel;
        mPivotX = mPivotX;
        mPivotYRel = mPivotYRel;
        mPivotY = mPivotY;
        mFramesCount = mFramesCount;
        mFrameDuration = mFrameDuration;
      }
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new AnimatedRotateDrawable(this, paramResources, null);
    }
  }
}
