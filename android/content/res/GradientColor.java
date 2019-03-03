package android.content.res;

import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R.styleable;
import com.android.internal.util.GrowingArrayUtils;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class GradientColor
  extends ComplexColor
{
  private static final boolean DBG_GRADIENT = false;
  private static final String TAG = "GradientColor";
  private static final int TILE_MODE_CLAMP = 0;
  private static final int TILE_MODE_MIRROR = 2;
  private static final int TILE_MODE_REPEAT = 1;
  private int mCenterColor = 0;
  private float mCenterX = 0.0F;
  private float mCenterY = 0.0F;
  private int mChangingConfigurations;
  private int mDefaultColor;
  private int mEndColor = 0;
  private float mEndX = 0.0F;
  private float mEndY = 0.0F;
  private GradientColorFactory mFactory;
  private float mGradientRadius = 0.0F;
  private int mGradientType = 0;
  private boolean mHasCenterColor = false;
  private int[] mItemColors;
  private float[] mItemOffsets;
  private int[][] mItemsThemeAttrs;
  private Shader mShader = null;
  private int mStartColor = 0;
  private float mStartX = 0.0F;
  private float mStartY = 0.0F;
  private int[] mThemeAttrs;
  private int mTileMode = 0;
  
  private GradientColor() {}
  
  private GradientColor(GradientColor paramGradientColor)
  {
    if (paramGradientColor != null)
    {
      mChangingConfigurations = mChangingConfigurations;
      mDefaultColor = mDefaultColor;
      mShader = mShader;
      mGradientType = mGradientType;
      mCenterX = mCenterX;
      mCenterY = mCenterY;
      mStartX = mStartX;
      mStartY = mStartY;
      mEndX = mEndX;
      mEndY = mEndY;
      mStartColor = mStartColor;
      mCenterColor = mCenterColor;
      mEndColor = mEndColor;
      mHasCenterColor = mHasCenterColor;
      mGradientRadius = mGradientRadius;
      mTileMode = mTileMode;
      if (mItemColors != null) {
        mItemColors = ((int[])mItemColors.clone());
      }
      if (mItemOffsets != null) {
        mItemOffsets = ((float[])mItemOffsets.clone());
      }
      if (mThemeAttrs != null) {
        mThemeAttrs = ((int[])mThemeAttrs.clone());
      }
      if (mItemsThemeAttrs != null) {
        mItemsThemeAttrs = ((int[][])mItemsThemeAttrs.clone());
      }
    }
  }
  
  private void applyItemsAttrsTheme(Resources.Theme paramTheme)
  {
    if (mItemsThemeAttrs == null) {
      return;
    }
    int[][] arrayOfInt = mItemsThemeAttrs;
    int i = arrayOfInt.length;
    int j = 0;
    int k = 0;
    while (k < i)
    {
      int m = j;
      if (arrayOfInt[k] != null)
      {
        TypedArray localTypedArray = paramTheme.resolveAttributes(arrayOfInt[k], R.styleable.GradientColorItem);
        arrayOfInt[k] = localTypedArray.extractThemeAttrs(arrayOfInt[k]);
        if (arrayOfInt[k] != null) {
          j = 1;
        }
        mItemColors[k] = localTypedArray.getColor(0, mItemColors[k]);
        mItemOffsets[k] = localTypedArray.getFloat(1, mItemOffsets[k]);
        mChangingConfigurations |= localTypedArray.getChangingConfigurations();
        localTypedArray.recycle();
        m = j;
      }
      k++;
      j = m;
    }
    if (j == 0) {
      mItemsThemeAttrs = null;
    }
  }
  
  private void applyRootAttrsTheme(Resources.Theme paramTheme)
  {
    paramTheme = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.GradientColor);
    mThemeAttrs = paramTheme.extractThemeAttrs(mThemeAttrs);
    updateRootElementState(paramTheme);
    mChangingConfigurations |= paramTheme.getChangingConfigurations();
    paramTheme.recycle();
  }
  
  private void applyTheme(Resources.Theme paramTheme)
  {
    if (mThemeAttrs != null) {
      applyRootAttrsTheme(paramTheme);
    }
    if (mItemsThemeAttrs != null) {
      applyItemsAttrsTheme(paramTheme);
    }
    onColorsChange();
  }
  
  public static GradientColor createFromXml(Resources paramResources, XmlResourceParser paramXmlResourceParser, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    AttributeSet localAttributeSet = Xml.asAttributeSet(paramXmlResourceParser);
    int i;
    do
    {
      i = paramXmlResourceParser.next();
    } while ((i != 2) && (i != 1));
    if (i == 2) {
      return createFromXmlInner(paramResources, paramXmlResourceParser, localAttributeSet, paramTheme);
    }
    throw new XmlPullParserException("No start tag found");
  }
  
  static GradientColor createFromXmlInner(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    Object localObject = paramXmlPullParser.getName();
    if (((String)localObject).equals("gradient"))
    {
      localObject = new GradientColor();
      ((GradientColor)localObject).inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      return localObject;
    }
    paramResources = new StringBuilder();
    paramResources.append(paramXmlPullParser.getPositionDescription());
    paramResources.append(": invalid gradient color tag ");
    paramResources.append((String)localObject);
    throw new XmlPullParserException(paramResources.toString());
  }
  
  private void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = Resources.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.GradientColor);
    updateRootElementState(localTypedArray);
    mChangingConfigurations |= localTypedArray.getChangingConfigurations();
    localTypedArray.recycle();
    validateXmlContent();
    inflateChildElements(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    onColorsChange();
  }
  
  private void inflateChildElements(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth() + 1;
    float[] arrayOfFloat = new float[20];
    int[] arrayOfInt1 = new int[arrayOfFloat.length];
    Object localObject = new int[arrayOfFloat.length][];
    int j = 0;
    int k = 0;
    for (;;)
    {
      int m = paramXmlPullParser.next();
      if (m == 1) {
        break;
      }
      int n = paramXmlPullParser.getDepth();
      if ((n < i) && (m == 3)) {
        break;
      }
      if (m == 2) {
        for (;;)
        {
          if (n <= i) {
            if (paramXmlPullParser.getName().equals("item"))
            {
              TypedArray localTypedArray = Resources.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.GradientColorItem);
              boolean bool1 = localTypedArray.hasValue(0);
              boolean bool2 = localTypedArray.hasValue(1);
              if ((bool1) && (bool2))
              {
                int[] arrayOfInt2 = localTypedArray.extractThemeAttrs();
                m = localTypedArray.getColor(0, 0);
                float f = localTypedArray.getFloat(1, 0.0F);
                mChangingConfigurations |= localTypedArray.getChangingConfigurations();
                localTypedArray.recycle();
                if (arrayOfInt2 != null) {
                  k = 1;
                }
                arrayOfInt1 = GrowingArrayUtils.append(arrayOfInt1, j, m);
                arrayOfFloat = GrowingArrayUtils.append(arrayOfFloat, j, f);
                localObject = (int[][])GrowingArrayUtils.append((Object[])localObject, j, arrayOfInt2);
                j++;
                break;
              }
              paramResources = new StringBuilder();
              paramResources.append(paramXmlPullParser.getPositionDescription());
              paramResources.append(": <item> tag requires a 'color' attribute and a 'offset' attribute!");
              throw new XmlPullParserException(paramResources.toString());
            }
          }
        }
      }
    }
    if (j > 0)
    {
      if (k != 0)
      {
        mItemsThemeAttrs = new int[j][];
        System.arraycopy(localObject, 0, mItemsThemeAttrs, 0, j);
      }
      else
      {
        mItemsThemeAttrs = null;
      }
      mItemColors = new int[j];
      mItemOffsets = new float[j];
      System.arraycopy(arrayOfInt1, 0, mItemColors, 0, j);
      System.arraycopy(arrayOfFloat, 0, mItemOffsets, 0, j);
    }
  }
  
  private void onColorsChange()
  {
    float[] arrayOfFloat = null;
    int[] arrayOfInt;
    if (mItemColors != null)
    {
      int i = mItemColors.length;
      arrayOfInt = new int[i];
      arrayOfFloat = new float[i];
      for (int j = 0; j < i; j++)
      {
        arrayOfInt[j] = mItemColors[j];
        arrayOfFloat[j] = mItemOffsets[j];
      }
    }
    else if (mHasCenterColor)
    {
      arrayOfInt = new int[3];
      arrayOfInt[0] = mStartColor;
      arrayOfInt[1] = mCenterColor;
      arrayOfInt[2] = mEndColor;
      arrayOfFloat = new float[3];
      arrayOfFloat[0] = 0.0F;
      arrayOfFloat[1] = 0.5F;
      arrayOfFloat[2] = 1.0F;
    }
    else
    {
      arrayOfInt = new int[2];
      arrayOfInt[0] = mStartColor;
      arrayOfInt[1] = mEndColor;
    }
    if (arrayOfInt.length < 2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<gradient> tag requires 2 color values specified!");
      localStringBuilder.append(arrayOfInt.length);
      localStringBuilder.append(" ");
      localStringBuilder.append(arrayOfInt);
      Log.w("GradientColor", localStringBuilder.toString());
    }
    if (mGradientType == 0) {
      mShader = new LinearGradient(mStartX, mStartY, mEndX, mEndY, arrayOfInt, arrayOfFloat, parseTileMode(mTileMode));
    } else if (mGradientType == 1) {
      mShader = new RadialGradient(mCenterX, mCenterY, mGradientRadius, arrayOfInt, arrayOfFloat, parseTileMode(mTileMode));
    } else {
      mShader = new SweepGradient(mCenterX, mCenterY, arrayOfInt, arrayOfFloat);
    }
    mDefaultColor = arrayOfInt[0];
  }
  
  private static Shader.TileMode parseTileMode(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Shader.TileMode.CLAMP;
    case 2: 
      return Shader.TileMode.MIRROR;
    case 1: 
      return Shader.TileMode.REPEAT;
    }
    return Shader.TileMode.CLAMP;
  }
  
  private void updateRootElementState(TypedArray paramTypedArray)
  {
    mThemeAttrs = paramTypedArray.extractThemeAttrs();
    mStartX = paramTypedArray.getFloat(8, mStartX);
    mStartY = paramTypedArray.getFloat(9, mStartY);
    mEndX = paramTypedArray.getFloat(10, mEndX);
    mEndY = paramTypedArray.getFloat(11, mEndY);
    mCenterX = paramTypedArray.getFloat(3, mCenterX);
    mCenterY = paramTypedArray.getFloat(4, mCenterY);
    mGradientType = paramTypedArray.getInt(2, mGradientType);
    mStartColor = paramTypedArray.getColor(0, mStartColor);
    mHasCenterColor |= paramTypedArray.hasValue(7);
    mCenterColor = paramTypedArray.getColor(7, mCenterColor);
    mEndColor = paramTypedArray.getColor(1, mEndColor);
    mTileMode = paramTypedArray.getInt(6, mTileMode);
    mGradientRadius = paramTypedArray.getFloat(5, mGradientRadius);
  }
  
  private void validateXmlContent()
    throws XmlPullParserException
  {
    if ((mGradientRadius <= 0.0F) && (mGradientType == 1)) {
      throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
    }
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if ((mThemeAttrs == null) && (mItemsThemeAttrs == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mChangingConfigurations;
  }
  
  public ConstantState<ComplexColor> getConstantState()
  {
    if (mFactory == null) {
      mFactory = new GradientColorFactory(this);
    }
    return mFactory;
  }
  
  public int getDefaultColor()
  {
    return mDefaultColor;
  }
  
  public Shader getShader()
  {
    return mShader;
  }
  
  public GradientColor obtainForTheme(Resources.Theme paramTheme)
  {
    if ((paramTheme != null) && (canApplyTheme()))
    {
      GradientColor localGradientColor = new GradientColor(this);
      localGradientColor.applyTheme(paramTheme);
      return localGradientColor;
    }
    return this;
  }
  
  private static class GradientColorFactory
    extends ConstantState<ComplexColor>
  {
    private final GradientColor mSrc;
    
    public GradientColorFactory(GradientColor paramGradientColor)
    {
      mSrc = paramGradientColor;
    }
    
    public int getChangingConfigurations()
    {
      return mSrc.mChangingConfigurations;
    }
    
    public GradientColor newInstance()
    {
      return mSrc;
    }
    
    public GradientColor newInstance(Resources paramResources, Resources.Theme paramTheme)
    {
      return mSrc.obtainForTheme(paramTheme);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  private static @interface GradientTileMode {}
}
