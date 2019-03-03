package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.Insets;
import android.graphics.NinePatch;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Xfermode;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.StateSet;
import android.util.TypedValue;
import android.util.Xml;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class Drawable
{
  static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
  private static final Rect ZERO_BOUNDS_RECT = new Rect();
  private Rect mBounds = ZERO_BOUNDS_RECT;
  private WeakReference<Callback> mCallback = null;
  private int mChangingConfigurations = 0;
  private int mLayoutDirection;
  private int mLevel = 0;
  protected int mSrcDensityOverride = 0;
  private int[] mStateSet = StateSet.WILD_CARD;
  private boolean mVisible = true;
  
  public Drawable() {}
  
  /* Error */
  public static Drawable createFromPath(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +5 -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: ldc2_w 71
    //   9: aload_0
    //   10: invokestatic 78	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   13: new 80	java/io/FileInputStream
    //   16: astore_1
    //   17: aload_1
    //   18: aload_0
    //   19: invokespecial 83	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   22: aconst_null
    //   23: aconst_null
    //   24: aload_1
    //   25: invokestatic 87	android/graphics/drawable/Drawable:getBitmapDrawable	(Landroid/content/res/Resources;Landroid/util/TypedValue;Ljava/io/InputStream;)Landroid/graphics/drawable/Drawable;
    //   28: astore_0
    //   29: aload_1
    //   30: invokevirtual 90	java/io/FileInputStream:close	()V
    //   33: ldc2_w 71
    //   36: invokestatic 94	android/os/Trace:traceEnd	(J)V
    //   39: aload_0
    //   40: areturn
    //   41: astore_2
    //   42: aconst_null
    //   43: astore_0
    //   44: goto +7 -> 51
    //   47: astore_0
    //   48: aload_0
    //   49: athrow
    //   50: astore_2
    //   51: aload_0
    //   52: ifnull +19 -> 71
    //   55: aload_1
    //   56: invokevirtual 90	java/io/FileInputStream:close	()V
    //   59: goto +16 -> 75
    //   62: astore_1
    //   63: aload_0
    //   64: aload_1
    //   65: invokevirtual 98	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   68: goto +7 -> 75
    //   71: aload_1
    //   72: invokevirtual 90	java/io/FileInputStream:close	()V
    //   75: aload_2
    //   76: athrow
    //   77: astore_0
    //   78: ldc2_w 71
    //   81: invokestatic 94	android/os/Trace:traceEnd	(J)V
    //   84: aload_0
    //   85: athrow
    //   86: astore_0
    //   87: ldc2_w 71
    //   90: invokestatic 94	android/os/Trace:traceEnd	(J)V
    //   93: aconst_null
    //   94: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	95	0	paramString	String
    //   16	40	1	localFileInputStream	java.io.FileInputStream
    //   62	10	1	localThrowable	Throwable
    //   41	1	2	localObject1	Object
    //   50	26	2	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   22	29	41	finally
    //   22	29	47	java/lang/Throwable
    //   48	50	50	finally
    //   55	59	62	java/lang/Throwable
    //   13	22	77	finally
    //   29	33	77	finally
    //   55	59	77	finally
    //   63	68	77	finally
    //   71	75	77	finally
    //   75	77	77	finally
    //   13	22	86	java/io/IOException
    //   29	33	86	java/io/IOException
    //   55	59	86	java/io/IOException
    //   63	68	86	java/io/IOException
    //   71	75	86	java/io/IOException
    //   75	77	86	java/io/IOException
  }
  
  public static Drawable createFromResourceStream(Resources paramResources, TypedValue paramTypedValue, InputStream paramInputStream, String paramString)
  {
    String str;
    if (paramString != null) {
      str = paramString;
    } else {
      str = "Unknown drawable";
    }
    Trace.traceBegin(8192L, str);
    try
    {
      paramResources = createFromResourceStream(paramResources, paramTypedValue, paramInputStream, paramString, null);
      return paramResources;
    }
    finally
    {
      Trace.traceEnd(8192L);
    }
  }
  
  public static Drawable createFromResourceStream(Resources paramResources, TypedValue paramTypedValue, InputStream paramInputStream, String paramString, BitmapFactory.Options paramOptions)
  {
    if (paramInputStream == null) {
      return null;
    }
    if (paramOptions == null) {
      return getBitmapDrawable(paramResources, paramTypedValue, paramInputStream);
    }
    Rect localRect = new Rect();
    inScreenDensity = resolveDensity(paramResources, 0);
    Bitmap localBitmap = BitmapFactory.decodeResourceStream(paramResources, paramTypedValue, paramInputStream, localRect, paramOptions);
    if (localBitmap != null)
    {
      paramOptions = localBitmap.getNinePatchChunk();
      if (paramOptions != null)
      {
        paramTypedValue = paramOptions;
        paramInputStream = localRect;
        if (NinePatch.isNinePatchChunk(paramOptions)) {}
      }
      else
      {
        paramTypedValue = null;
        paramInputStream = null;
      }
      paramOptions = new Rect();
      localBitmap.getOpticalInsets(paramOptions);
      return drawableFromBitmap(paramResources, localBitmap, paramTypedValue, paramInputStream, paramOptions, paramString);
    }
    return null;
  }
  
  public static Drawable createFromStream(InputStream paramInputStream, String paramString)
  {
    String str;
    if (paramString != null) {
      str = paramString;
    } else {
      str = "Unknown drawable";
    }
    Trace.traceBegin(8192L, str);
    try
    {
      paramInputStream = createFromResourceStream(null, null, paramInputStream, paramString);
      return paramInputStream;
    }
    finally
    {
      Trace.traceEnd(8192L);
    }
  }
  
  public static Drawable createFromXml(Resources paramResources, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    return createFromXml(paramResources, paramXmlPullParser, null);
  }
  
  public static Drawable createFromXml(Resources paramResources, XmlPullParser paramXmlPullParser, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    return createFromXmlForDensity(paramResources, paramXmlPullParser, 0, paramTheme);
  }
  
  public static Drawable createFromXmlForDensity(Resources paramResources, XmlPullParser paramXmlPullParser, int paramInt, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    AttributeSet localAttributeSet = Xml.asAttributeSet(paramXmlPullParser);
    int i;
    do
    {
      i = paramXmlPullParser.next();
    } while ((i != 2) && (i != 1));
    if (i == 2)
    {
      paramResources = createFromXmlInnerForDensity(paramResources, paramXmlPullParser, localAttributeSet, paramInt, paramTheme);
      if (paramResources != null) {
        return paramResources;
      }
      paramResources = new StringBuilder();
      paramResources.append("Unknown initial tag: ");
      paramResources.append(paramXmlPullParser.getName());
      throw new RuntimeException(paramResources.toString());
    }
    throw new XmlPullParserException("No start tag found");
  }
  
  public static Drawable createFromXmlInner(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    return createFromXmlInner(paramResources, paramXmlPullParser, paramAttributeSet, null);
  }
  
  public static Drawable createFromXmlInner(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    return createFromXmlInnerForDensity(paramResources, paramXmlPullParser, paramAttributeSet, 0, paramTheme);
  }
  
  static Drawable createFromXmlInnerForDensity(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, int paramInt, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    return paramResources.getDrawableInflater().inflateFromXmlForDensity(paramXmlPullParser.getName(), paramXmlPullParser, paramAttributeSet, paramInt, paramTheme);
  }
  
  private static Drawable drawableFromBitmap(Resources paramResources, Bitmap paramBitmap, byte[] paramArrayOfByte, Rect paramRect1, Rect paramRect2, String paramString)
  {
    if (paramArrayOfByte != null) {
      return new NinePatchDrawable(paramResources, paramBitmap, paramArrayOfByte, paramRect1, paramRect2, paramString);
    }
    return new BitmapDrawable(paramResources, paramBitmap);
  }
  
  private static Drawable getBitmapDrawable(Resources paramResources, TypedValue paramTypedValue, InputStream paramInputStream)
  {
    if (paramTypedValue != null)
    {
      int i = 0;
      try
      {
        if (density == 0) {
          i = 160;
        } else if (density != 65535) {
          i = density;
        }
        paramResources = ImageDecoder.createSource(paramResources, paramInputStream, i);
      }
      catch (IOException paramTypedValue)
      {
        break label64;
      }
    }
    paramResources = ImageDecoder.createSource(paramResources, paramInputStream);
    paramResources = ImageDecoder.decodeDrawable(paramResources, _..Lambda.Drawable.wmqxcnFJRLY7tFDmv2eEGR5vtvU.INSTANCE);
    return paramResources;
    label64:
    paramResources = new StringBuilder();
    paramResources.append("Unable to decode stream: ");
    paramResources.append(paramTypedValue);
    Log.e("Drawable", paramResources.toString());
    return null;
  }
  
  protected static TypedArray obtainAttributes(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfInt)
  {
    if (paramTheme == null) {
      return paramResources.obtainAttributes(paramAttributeSet, paramArrayOfInt);
    }
    return paramTheme.obtainStyledAttributes(paramAttributeSet, paramArrayOfInt, 0, 0);
  }
  
  public static PorterDuff.Mode parseTintMode(int paramInt, PorterDuff.Mode paramMode)
  {
    if (paramInt != 3)
    {
      if (paramInt != 5)
      {
        if (paramInt != 9)
        {
          switch (paramInt)
          {
          default: 
            return paramMode;
          case 16: 
            return PorterDuff.Mode.ADD;
          case 15: 
            return PorterDuff.Mode.SCREEN;
          }
          return PorterDuff.Mode.MULTIPLY;
        }
        return PorterDuff.Mode.SRC_ATOP;
      }
      return PorterDuff.Mode.SRC_IN;
    }
    return PorterDuff.Mode.SRC_OVER;
  }
  
  static int resolveDensity(Resources paramResources, int paramInt)
  {
    if (paramResources != null) {
      paramInt = getDisplayMetricsdensityDpi;
    }
    if (paramInt == 0) {
      paramInt = 160;
    }
    return paramInt;
  }
  
  public static int resolveOpacity(int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2) {
      return paramInt1;
    }
    if ((paramInt1 != 0) && (paramInt2 != 0))
    {
      if ((paramInt1 != -3) && (paramInt2 != -3))
      {
        if ((paramInt1 != -2) && (paramInt2 != -2)) {
          return -1;
        }
        return -2;
      }
      return -3;
    }
    return 0;
  }
  
  static void rethrowAsRuntimeException(Exception paramException)
    throws RuntimeException
  {
    paramException = new RuntimeException(paramException);
    paramException.setStackTrace(new StackTraceElement[0]);
    throw paramException;
  }
  
  static float scaleFromDensity(float paramFloat, int paramInt1, int paramInt2)
  {
    return paramInt2 * paramFloat / paramInt1;
  }
  
  static int scaleFromDensity(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    if ((paramInt1 != 0) && (paramInt2 != paramInt3))
    {
      float f = paramInt1 * paramInt3 / paramInt2;
      if (!paramBoolean) {
        return (int)f;
      }
      paramInt2 = Math.round(f);
      if (paramInt2 != 0) {
        return paramInt2;
      }
      if (paramInt1 > 0) {
        return 1;
      }
      return -1;
    }
    return paramInt1;
  }
  
  public void applyTheme(Resources.Theme paramTheme) {}
  
  public boolean canApplyTheme()
  {
    return false;
  }
  
  public void clearColorFilter()
  {
    setColorFilter(null);
  }
  
  public void clearMutated() {}
  
  public final Rect copyBounds()
  {
    return new Rect(mBounds);
  }
  
  public final void copyBounds(Rect paramRect)
  {
    paramRect.set(mBounds);
  }
  
  public abstract void draw(Canvas paramCanvas);
  
  public int getAlpha()
  {
    return 255;
  }
  
  public final Rect getBounds()
  {
    if (mBounds == ZERO_BOUNDS_RECT) {
      mBounds = new Rect();
    }
    return mBounds;
  }
  
  public Callback getCallback()
  {
    Callback localCallback;
    if (mCallback != null) {
      localCallback = (Callback)mCallback.get();
    } else {
      localCallback = null;
    }
    return localCallback;
  }
  
  public int getChangingConfigurations()
  {
    return mChangingConfigurations;
  }
  
  public ColorFilter getColorFilter()
  {
    return null;
  }
  
  public ConstantState getConstantState()
  {
    return null;
  }
  
  public Drawable getCurrent()
  {
    return this;
  }
  
  public Rect getDirtyBounds()
  {
    return getBounds();
  }
  
  public void getHotspotBounds(Rect paramRect)
  {
    paramRect.set(getBounds());
  }
  
  public int getIntrinsicHeight()
  {
    return -1;
  }
  
  public int getIntrinsicWidth()
  {
    return -1;
  }
  
  public int getLayoutDirection()
  {
    return mLayoutDirection;
  }
  
  public final int getLevel()
  {
    return mLevel;
  }
  
  public int getMinimumHeight()
  {
    int i = getIntrinsicHeight();
    if (i <= 0) {
      i = 0;
    }
    return i;
  }
  
  public int getMinimumWidth()
  {
    int i = getIntrinsicWidth();
    if (i <= 0) {
      i = 0;
    }
    return i;
  }
  
  public abstract int getOpacity();
  
  public Insets getOpticalInsets()
  {
    return Insets.NONE;
  }
  
  public void getOutline(Outline paramOutline)
  {
    paramOutline.setRect(getBounds());
    paramOutline.setAlpha(0.0F);
  }
  
  public boolean getPadding(Rect paramRect)
  {
    paramRect.set(0, 0, 0, 0);
    return false;
  }
  
  public int[] getState()
  {
    return mStateSet;
  }
  
  public Region getTransparentRegion()
  {
    return null;
  }
  
  public boolean hasFocusStateSpecified()
  {
    return false;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    inflate(paramResources, paramXmlPullParser, paramAttributeSet, null);
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    paramResources = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.Drawable);
    mVisible = paramResources.getBoolean(0, mVisible);
    paramResources.recycle();
  }
  
  void inflateWithAttributes(Resources paramResources, XmlPullParser paramXmlPullParser, TypedArray paramTypedArray, int paramInt)
    throws XmlPullParserException, IOException
  {
    mVisible = paramTypedArray.getBoolean(paramInt, mVisible);
  }
  
  public void invalidateSelf()
  {
    Callback localCallback = getCallback();
    if (localCallback != null) {
      localCallback.invalidateDrawable(this);
    }
  }
  
  public boolean isAutoMirrored()
  {
    return false;
  }
  
  public boolean isFilterBitmap()
  {
    return false;
  }
  
  public boolean isProjected()
  {
    return false;
  }
  
  public boolean isStateful()
  {
    return false;
  }
  
  public final boolean isVisible()
  {
    return mVisible;
  }
  
  public void jumpToCurrentState() {}
  
  public Drawable mutate()
  {
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect) {}
  
  public boolean onLayoutDirectionChanged(int paramInt)
  {
    return false;
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    return false;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    return false;
  }
  
  public void scheduleSelf(Runnable paramRunnable, long paramLong)
  {
    Callback localCallback = getCallback();
    if (localCallback != null) {
      localCallback.scheduleDrawable(this, paramRunnable, paramLong);
    }
  }
  
  public abstract void setAlpha(int paramInt);
  
  public void setAutoMirrored(boolean paramBoolean) {}
  
  public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Rect localRect1 = mBounds;
    Rect localRect2 = localRect1;
    if (localRect1 == ZERO_BOUNDS_RECT)
    {
      localRect2 = new Rect();
      mBounds = localRect2;
    }
    if ((left != paramInt1) || (top != paramInt2) || (right != paramInt3) || (bottom != paramInt4))
    {
      if (!localRect2.isEmpty()) {
        invalidateSelf();
      }
      mBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
      onBoundsChange(mBounds);
    }
  }
  
  public void setBounds(Rect paramRect)
  {
    setBounds(left, top, right, bottom);
  }
  
  public final void setCallback(Callback paramCallback)
  {
    if (paramCallback != null) {
      paramCallback = new WeakReference(paramCallback);
    } else {
      paramCallback = null;
    }
    mCallback = paramCallback;
  }
  
  public void setChangingConfigurations(int paramInt)
  {
    mChangingConfigurations = paramInt;
  }
  
  public void setColorFilter(int paramInt, PorterDuff.Mode paramMode)
  {
    if ((getColorFilter() instanceof PorterDuffColorFilter))
    {
      PorterDuffColorFilter localPorterDuffColorFilter = (PorterDuffColorFilter)getColorFilter();
      if ((localPorterDuffColorFilter.getColor() == paramInt) && (localPorterDuffColorFilter.getMode() == paramMode)) {
        return;
      }
    }
    setColorFilter(new PorterDuffColorFilter(paramInt, paramMode));
  }
  
  public abstract void setColorFilter(ColorFilter paramColorFilter);
  
  @Deprecated
  public void setDither(boolean paramBoolean) {}
  
  public void setFilterBitmap(boolean paramBoolean) {}
  
  public void setHotspot(float paramFloat1, float paramFloat2) {}
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public final boolean setLayoutDirection(int paramInt)
  {
    if (mLayoutDirection != paramInt)
    {
      mLayoutDirection = paramInt;
      return onLayoutDirectionChanged(paramInt);
    }
    return false;
  }
  
  public final boolean setLevel(int paramInt)
  {
    if (mLevel != paramInt)
    {
      mLevel = paramInt;
      return onLevelChange(paramInt);
    }
    return false;
  }
  
  final void setSrcDensityOverride(int paramInt)
  {
    mSrcDensityOverride = paramInt;
  }
  
  public boolean setState(int[] paramArrayOfInt)
  {
    if (!Arrays.equals(mStateSet, paramArrayOfInt))
    {
      mStateSet = paramArrayOfInt;
      return onStateChange(paramArrayOfInt);
    }
    return false;
  }
  
  public void setTint(int paramInt)
  {
    setTintList(ColorStateList.valueOf(paramInt));
  }
  
  public void setTintList(ColorStateList paramColorStateList) {}
  
  public void setTintMode(PorterDuff.Mode paramMode) {}
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mVisible != paramBoolean1) {
      paramBoolean2 = true;
    } else {
      paramBoolean2 = false;
    }
    if (paramBoolean2)
    {
      mVisible = paramBoolean1;
      invalidateSelf();
    }
    return paramBoolean2;
  }
  
  public void setXfermode(Xfermode paramXfermode) {}
  
  public void unscheduleSelf(Runnable paramRunnable)
  {
    Callback localCallback = getCallback();
    if (localCallback != null) {
      localCallback.unscheduleDrawable(this, paramRunnable);
    }
  }
  
  PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter paramPorterDuffColorFilter, ColorStateList paramColorStateList, PorterDuff.Mode paramMode)
  {
    if ((paramColorStateList != null) && (paramMode != null))
    {
      int i = paramColorStateList.getColorForState(getState(), 0);
      if (paramPorterDuffColorFilter == null) {
        return new PorterDuffColorFilter(i, paramMode);
      }
      paramPorterDuffColorFilter.setColor(i);
      paramPorterDuffColorFilter.setMode(paramMode);
      return paramPorterDuffColorFilter;
    }
    return null;
  }
  
  public static abstract interface Callback
  {
    public abstract void invalidateDrawable(Drawable paramDrawable);
    
    public abstract void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong);
    
    public abstract void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable);
  }
  
  public static abstract class ConstantState
  {
    public ConstantState() {}
    
    public boolean canApplyTheme()
    {
      return false;
    }
    
    public abstract int getChangingConfigurations();
    
    public abstract Drawable newDrawable();
    
    public Drawable newDrawable(Resources paramResources)
    {
      return newDrawable();
    }
    
    public Drawable newDrawable(Resources paramResources, Resources.Theme paramTheme)
    {
      return newDrawable(paramResources);
    }
  }
}
