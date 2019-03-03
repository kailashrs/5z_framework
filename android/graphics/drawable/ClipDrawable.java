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

public class ClipDrawable
  extends DrawableWrapper
{
  public static final int HORIZONTAL = 1;
  private static final int MAX_LEVEL = 10000;
  public static final int VERTICAL = 2;
  private ClipState mState;
  private final Rect mTmpRect = new Rect();
  
  ClipDrawable()
  {
    this(new ClipState(null, null), null);
  }
  
  private ClipDrawable(ClipState paramClipState, Resources paramResources)
  {
    super(paramClipState, paramResources);
    mState = paramClipState;
  }
  
  public ClipDrawable(Drawable paramDrawable, int paramInt1, int paramInt2)
  {
    this(new ClipState(null, null), null);
    mState.mGravity = paramInt1;
    mState.mOrientation = paramInt2;
    setDrawable(paramDrawable);
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    ClipState localClipState = mState;
    if (localClipState == null) {
      return;
    }
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    ClipState.access$002(localClipState, paramTypedArray.extractThemeAttrs());
    mOrientation = paramTypedArray.getInt(2, mOrientation);
    mGravity = paramTypedArray.getInt(0, mGravity);
  }
  
  private void verifyRequiredAttributes(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[1] == 0)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramTypedArray.getPositionDescription());
      localStringBuilder.append(": <clip> tag requires a 'drawable' attribute or child tag defining a drawable");
      throw new XmlPullParserException(localStringBuilder.toString());
    }
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 108	android/graphics/drawable/DrawableWrapper:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 38	android/graphics/drawable/ClipDrawable:mState	Landroid/graphics/drawable/ClipDrawable$ClipState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_2
    //   16: invokestatic 84	android/graphics/drawable/ClipDrawable$ClipState:access$000	(Landroid/graphics/drawable/ClipDrawable$ClipState;)[I
    //   19: ifnull +50 -> 69
    //   22: aload_1
    //   23: aload_2
    //   24: invokestatic 84	android/graphics/drawable/ClipDrawable$ClipState:access$000	(Landroid/graphics/drawable/ClipDrawable$ClipState;)[I
    //   27: getstatic 114	com/android/internal/R$styleable:ClipDrawable	[I
    //   30: invokevirtual 120	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   33: astore_1
    //   34: aload_0
    //   35: aload_1
    //   36: invokespecial 122	android/graphics/drawable/ClipDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;)V
    //   39: aload_0
    //   40: aload_1
    //   41: invokespecial 124	android/graphics/drawable/ClipDrawable:verifyRequiredAttributes	(Landroid/content/res/TypedArray;)V
    //   44: aload_1
    //   45: invokevirtual 127	android/content/res/TypedArray:recycle	()V
    //   48: goto +21 -> 69
    //   51: astore_2
    //   52: goto +11 -> 63
    //   55: astore_2
    //   56: aload_2
    //   57: invokestatic 131	android/graphics/drawable/ClipDrawable:rethrowAsRuntimeException	(Ljava/lang/Exception;)V
    //   60: goto -16 -> 44
    //   63: aload_1
    //   64: invokevirtual 127	android/content/res/TypedArray:recycle	()V
    //   67: aload_2
    //   68: athrow
    //   69: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	70	0	this	ClipDrawable
    //   0	70	1	paramTheme	Resources.Theme
    //   9	15	2	localClipState	ClipState
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
    if (localDrawable.getLevel() == 0) {
      return;
    }
    Rect localRect1 = mTmpRect;
    Rect localRect2 = getBounds();
    int i = getLevel();
    int j = localRect2.width();
    int k = j;
    if ((mState.mOrientation & 0x1) != 0) {
      k = j - (j + 0) * (10000 - i) / 10000;
    }
    int m = localRect2.height();
    j = m;
    if ((mState.mOrientation & 0x2) != 0) {
      j = m - (m + 0) * (10000 - i) / 10000;
    }
    m = getLayoutDirection();
    Gravity.apply(mState.mGravity, k, j, localRect2, localRect1, m);
    if ((k > 0) && (j > 0))
    {
      paramCanvas.save();
      paramCanvas.clipRect(localRect1);
      localDrawable.draw(paramCanvas);
      paramCanvas.restore();
    }
  }
  
  public int getOpacity()
  {
    Drawable localDrawable = getDrawable();
    if ((localDrawable.getOpacity() != -2) && (localDrawable.getLevel() != 0))
    {
      if (getLevel() >= 10000) {
        return localDrawable.getOpacity();
      }
      return -3;
    }
    return -2;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ClipDrawable);
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    updateStateFromTypedArray(localTypedArray);
    verifyRequiredAttributes(localTypedArray);
    localTypedArray.recycle();
  }
  
  DrawableWrapper.DrawableWrapperState mutateConstantState()
  {
    mState = new ClipState(mState, null);
    return mState;
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    super.onLevelChange(paramInt);
    invalidateSelf();
    return true;
  }
  
  static final class ClipState
    extends DrawableWrapper.DrawableWrapperState
  {
    int mGravity = 3;
    int mOrientation = 1;
    private int[] mThemeAttrs;
    
    ClipState(ClipState paramClipState, Resources paramResources)
    {
      super(paramResources);
      if (paramClipState != null)
      {
        mOrientation = mOrientation;
        mGravity = mGravity;
      }
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new ClipDrawable(this, paramResources, null);
    }
  }
}
