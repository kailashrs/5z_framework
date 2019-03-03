package android.content.res;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseArray;
import android.util.StateSet;
import android.util.Xml;
import com.android.internal.R.styleable;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ColorStateList
  extends ComplexColor
  implements Parcelable
{
  public static final Parcelable.Creator<ColorStateList> CREATOR = new Parcelable.Creator()
  {
    public ColorStateList createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int[][] arrayOfInt = new int[i][];
      for (int j = 0; j < i; j++) {
        arrayOfInt[j] = paramAnonymousParcel.createIntArray();
      }
      return new ColorStateList(arrayOfInt, paramAnonymousParcel.createIntArray());
    }
    
    public ColorStateList[] newArray(int paramAnonymousInt)
    {
      return new ColorStateList[paramAnonymousInt];
    }
  };
  private static final int DEFAULT_COLOR = -65536;
  private static final int[][] EMPTY = { new int[0] };
  private static final String TAG = "ColorStateList";
  private static final SparseArray<WeakReference<ColorStateList>> sCache = new SparseArray();
  private int mChangingConfigurations;
  private int[] mColors;
  private int mDefaultColor;
  private ColorStateListFactory mFactory;
  private boolean mIsOpaque;
  private int[][] mStateSpecs;
  private int[][] mThemeAttrs;
  
  private ColorStateList() {}
  
  private ColorStateList(ColorStateList paramColorStateList)
  {
    if (paramColorStateList != null)
    {
      mChangingConfigurations = mChangingConfigurations;
      mStateSpecs = mStateSpecs;
      mDefaultColor = mDefaultColor;
      mIsOpaque = mIsOpaque;
      mThemeAttrs = ((int[][])mThemeAttrs.clone());
      mColors = ((int[])mColors.clone());
    }
  }
  
  public ColorStateList(int[][] paramArrayOfInt, int[] paramArrayOfInt1)
  {
    mStateSpecs = paramArrayOfInt;
    mColors = paramArrayOfInt1;
    onColorsChanged();
  }
  
  private void applyTheme(Resources.Theme paramTheme)
  {
    if (mThemeAttrs == null) {
      return;
    }
    int[][] arrayOfInt = mThemeAttrs;
    int i = arrayOfInt.length;
    int j = 0;
    int k = 0;
    while (k < i)
    {
      int m = j;
      if (arrayOfInt[k] != null)
      {
        TypedArray localTypedArray = paramTheme.resolveAttributes(arrayOfInt[k], R.styleable.ColorStateListItem);
        if (arrayOfInt[k][0] != 0) {
          f = Color.alpha(mColors[k]) / 255.0F;
        } else {
          f = 1.0F;
        }
        arrayOfInt[k] = localTypedArray.extractThemeAttrs(arrayOfInt[k]);
        if (arrayOfInt[k] != null) {
          j = 1;
        }
        m = localTypedArray.getColor(0, mColors[k]);
        float f = localTypedArray.getFloat(1, f);
        mColors[k] = modulateColorAlpha(m, f);
        mChangingConfigurations |= localTypedArray.getChangingConfigurations();
        localTypedArray.recycle();
        m = j;
      }
      k++;
      j = m;
    }
    if (j == 0) {
      mThemeAttrs = null;
    }
    onColorsChanged();
  }
  
  @Deprecated
  public static ColorStateList createFromXml(Resources paramResources, XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    return createFromXml(paramResources, paramXmlPullParser, null);
  }
  
  public static ColorStateList createFromXml(Resources paramResources, XmlPullParser paramXmlPullParser, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    AttributeSet localAttributeSet = Xml.asAttributeSet(paramXmlPullParser);
    int i;
    do
    {
      i = paramXmlPullParser.next();
    } while ((i != 2) && (i != 1));
    if (i == 2) {
      return createFromXmlInner(paramResources, paramXmlPullParser, localAttributeSet, paramTheme);
    }
    throw new XmlPullParserException("No start tag found");
  }
  
  static ColorStateList createFromXmlInner(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    Object localObject = paramXmlPullParser.getName();
    if (((String)localObject).equals("selector"))
    {
      localObject = new ColorStateList();
      ((ColorStateList)localObject).inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      return localObject;
    }
    paramResources = new StringBuilder();
    paramResources.append(paramXmlPullParser.getPositionDescription());
    paramResources.append(": invalid color state list tag ");
    paramResources.append((String)localObject);
    throw new XmlPullParserException(paramResources.toString());
  }
  
  private void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth() + 1;
    int[][] arrayOfInt = (int[][])ArrayUtils.newUnpaddedArray([I.class, 20);
    Object localObject1 = new int[arrayOfInt.length][];
    int[] arrayOfInt1 = new int[arrayOfInt.length];
    int j = 0;
    int k = -65536;
    int m = 0;
    int n = 0;
    for (;;)
    {
      int i1 = paramXmlPullParser.next();
      if (i1 == 1) {
        break;
      }
      int i2 = paramXmlPullParser.getDepth();
      int i3 = i2;
      if ((i2 < i) && (i1 == 3)) {
        break;
      }
      if ((i1 == 2) && (i3 <= i) && (paramXmlPullParser.getName().equals("item")))
      {
        Object localObject2 = Resources.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ColorStateListItem);
        int[] arrayOfInt2 = ((TypedArray)localObject2).extractThemeAttrs();
        int i4 = ((TypedArray)localObject2).getColor(0, -65281);
        float f = ((TypedArray)localObject2).getFloat(1, 1.0F);
        int i5 = ((TypedArray)localObject2).getChangingConfigurations();
        ((TypedArray)localObject2).recycle();
        i2 = paramAttributeSet.getAttributeCount();
        localObject2 = new int[i2];
        int i6 = 0;
        for (i1 = 0; i1 < i2; i1++)
        {
          int i7 = paramAttributeSet.getAttributeNameResource(i1);
          if ((i7 != 16843173) && (i7 != 16843551))
          {
            if (!paramAttributeSet.getAttributeBooleanValue(i1, false)) {
              i7 = -i7;
            }
            localObject2[i6] = i7;
            i6++;
          }
        }
        localObject2 = StateSet.trimStateSet((int[])localObject2, i6);
        i2 = modulateColorAlpha(i4, f);
        if ((n == 0) || (localObject2.length == 0)) {
          k = i2;
        }
        if (arrayOfInt2 != null) {
          j = 1;
        }
        arrayOfInt1 = GrowingArrayUtils.append(arrayOfInt1, n, i2);
        localObject1 = (int[][])GrowingArrayUtils.append((Object[])localObject1, n, arrayOfInt2);
        arrayOfInt = (int[][])GrowingArrayUtils.append(arrayOfInt, n, localObject2);
        n++;
        m |= i5;
      }
    }
    mChangingConfigurations = m;
    mDefaultColor = k;
    if (j != 0)
    {
      mThemeAttrs = new int[n][];
      System.arraycopy(localObject1, 0, mThemeAttrs, 0, n);
    }
    else
    {
      mThemeAttrs = null;
    }
    mColors = new int[n];
    mStateSpecs = new int[n][];
    System.arraycopy(arrayOfInt1, 0, mColors, 0, n);
    System.arraycopy(arrayOfInt, 0, mStateSpecs, 0, n);
    onColorsChanged();
  }
  
  private int modulateColorAlpha(int paramInt, float paramFloat)
  {
    if (paramFloat == 1.0F) {
      return paramInt;
    }
    return 0xFFFFFF & paramInt | MathUtils.constrain((int)(Color.alpha(paramInt) * paramFloat + 0.5F), 0, 255) << 24;
  }
  
  private void onColorsChanged()
  {
    int i = -65536;
    boolean bool1 = true;
    int[][] arrayOfInt = mStateSpecs;
    int[] arrayOfInt1 = mColors;
    int j = arrayOfInt.length;
    boolean bool2 = bool1;
    if (j > 0)
    {
      int k = 0;
      int m = arrayOfInt1[0];
      int n;
      for (i = j - 1;; i--)
      {
        n = m;
        if (i <= 0) {
          break;
        }
        if (arrayOfInt[i].length == 0)
        {
          n = arrayOfInt1[i];
          break;
        }
      }
      for (;;)
      {
        i = n;
        bool2 = bool1;
        if (k >= j) {
          break;
        }
        if (Color.alpha(arrayOfInt1[k]) != 255)
        {
          bool2 = false;
          i = n;
          break;
        }
        k++;
      }
    }
    mDefaultColor = i;
    mIsOpaque = bool2;
  }
  
  public static ColorStateList valueOf(int paramInt)
  {
    synchronized (sCache)
    {
      int i = sCache.indexOfKey(paramInt);
      if (i >= 0)
      {
        localObject1 = (ColorStateList)((WeakReference)sCache.valueAt(i)).get();
        if (localObject1 != null) {
          return localObject1;
        }
        sCache.removeAt(i);
      }
      for (i = sCache.size() - 1; i >= 0; i--) {
        if (((WeakReference)sCache.valueAt(i)).get() == null) {
          sCache.removeAt(i);
        }
      }
      ColorStateList localColorStateList = new android/content/res/ColorStateList;
      localColorStateList.<init>(EMPTY, new int[] { paramInt });
      Object localObject1 = sCache;
      WeakReference localWeakReference = new java/lang/ref/WeakReference;
      localWeakReference.<init>(localColorStateList);
      ((SparseArray)localObject1).put(paramInt, localWeakReference);
      return localColorStateList;
    }
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if (mThemeAttrs != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mChangingConfigurations;
  }
  
  public int getColorForState(int[] paramArrayOfInt, int paramInt)
  {
    int i = mStateSpecs.length;
    for (int j = 0; j < i; j++) {
      if (StateSet.stateSetMatches(mStateSpecs[j], paramArrayOfInt)) {
        return mColors[j];
      }
    }
    return paramInt;
  }
  
  public int[] getColors()
  {
    return mColors;
  }
  
  public ConstantState<ComplexColor> getConstantState()
  {
    if (mFactory == null) {
      mFactory = new ColorStateListFactory(this);
    }
    return mFactory;
  }
  
  public int getDefaultColor()
  {
    return mDefaultColor;
  }
  
  public int[][] getStates()
  {
    return mStateSpecs;
  }
  
  public boolean hasFocusStateSpecified()
  {
    return StateSet.containsAttribute(mStateSpecs, 16842908);
  }
  
  public boolean hasState(int paramInt)
  {
    for (int[] arrayOfInt1 : mStateSpecs)
    {
      int k = arrayOfInt1.length;
      int m = 0;
      while (m < k) {
        if ((arrayOfInt1[m] != paramInt) && (arrayOfInt1[m] != paramInt)) {
          m++;
        } else {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean isOpaque()
  {
    return mIsOpaque;
  }
  
  public boolean isStateful()
  {
    int i = mStateSpecs.length;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (i >= 1)
    {
      bool2 = bool1;
      if (mStateSpecs[0].length > 0) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public ColorStateList obtainForTheme(Resources.Theme paramTheme)
  {
    if ((paramTheme != null) && (canApplyTheme()))
    {
      ColorStateList localColorStateList = new ColorStateList(this);
      localColorStateList.applyTheme(paramTheme);
      return localColorStateList;
    }
    return this;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ColorStateList{mThemeAttrs=");
    localStringBuilder.append(Arrays.deepToString(mThemeAttrs));
    localStringBuilder.append("mChangingConfigurations=");
    localStringBuilder.append(mChangingConfigurations);
    localStringBuilder.append("mStateSpecs=");
    localStringBuilder.append(Arrays.deepToString(mStateSpecs));
    localStringBuilder.append("mColors=");
    localStringBuilder.append(Arrays.toString(mColors));
    localStringBuilder.append("mDefaultColor=");
    localStringBuilder.append(mDefaultColor);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public ColorStateList withAlpha(int paramInt)
  {
    int[] arrayOfInt = new int[mColors.length];
    int i = arrayOfInt.length;
    for (int j = 0; j < i; j++) {
      arrayOfInt[j] = (mColors[j] & 0xFFFFFF | paramInt << 24);
    }
    return new ColorStateList(mStateSpecs, arrayOfInt);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (canApplyTheme()) {
      Log.w("ColorStateList", "Wrote partially-resolved ColorStateList to parcel!");
    }
    int i = mStateSpecs.length;
    paramParcel.writeInt(i);
    for (paramInt = 0; paramInt < i; paramInt++) {
      paramParcel.writeIntArray(mStateSpecs[paramInt]);
    }
    paramParcel.writeIntArray(mColors);
  }
  
  private static class ColorStateListFactory
    extends ConstantState<ComplexColor>
  {
    private final ColorStateList mSrc;
    
    public ColorStateListFactory(ColorStateList paramColorStateList)
    {
      mSrc = paramColorStateList;
    }
    
    public int getChangingConfigurations()
    {
      return mSrc.mChangingConfigurations;
    }
    
    public ColorStateList newInstance()
    {
      return mSrc;
    }
    
    public ColorStateList newInstance(Resources paramResources, Resources.Theme paramTheme)
    {
      return mSrc.obtainForTheme(paramTheme);
    }
  }
}
