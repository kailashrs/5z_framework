package android.graphics;

import android.util.Half;
import com.android.internal.util.XmlUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.DoubleUnaryOperator;

public class Color
{
  public static final int BLACK = -16777216;
  public static final int BLUE = -16776961;
  public static final int CYAN = -16711681;
  public static final int DKGRAY = -12303292;
  public static final int GRAY = -7829368;
  public static final int GREEN = -16711936;
  public static final int LTGRAY = -3355444;
  public static final int MAGENTA = -65281;
  public static final int RED = -65536;
  public static final int TRANSPARENT = 0;
  public static final int WHITE = -1;
  public static final int YELLOW = -256;
  private static final HashMap<String, Integer> sColorNameMap = new HashMap();
  private final ColorSpace mColorSpace;
  private final float[] mComponents;
  
  static
  {
    sColorNameMap.put("black", Integer.valueOf(-16777216));
    sColorNameMap.put("darkgray", Integer.valueOf(-12303292));
    sColorNameMap.put("gray", Integer.valueOf(-7829368));
    sColorNameMap.put("lightgray", Integer.valueOf(-3355444));
    sColorNameMap.put("white", Integer.valueOf(-1));
    sColorNameMap.put("red", Integer.valueOf(-65536));
    sColorNameMap.put("green", Integer.valueOf(-16711936));
    sColorNameMap.put("blue", Integer.valueOf(-16776961));
    sColorNameMap.put("yellow", Integer.valueOf(65280));
    sColorNameMap.put("cyan", Integer.valueOf(-16711681));
    sColorNameMap.put("magenta", Integer.valueOf(-65281));
    sColorNameMap.put("aqua", Integer.valueOf(-16711681));
    sColorNameMap.put("fuchsia", Integer.valueOf(-65281));
    sColorNameMap.put("darkgrey", Integer.valueOf(-12303292));
    sColorNameMap.put("grey", Integer.valueOf(-7829368));
    sColorNameMap.put("lightgrey", Integer.valueOf(-3355444));
    sColorNameMap.put("lime", Integer.valueOf(-16711936));
    sColorNameMap.put("maroon", Integer.valueOf(-8388608));
    sColorNameMap.put("navy", Integer.valueOf(-16777088));
    sColorNameMap.put("olive", Integer.valueOf(-8355840));
    sColorNameMap.put("purple", Integer.valueOf(-8388480));
    sColorNameMap.put("silver", Integer.valueOf(-4144960));
    sColorNameMap.put("teal", Integer.valueOf(-16744320));
  }
  
  public Color()
  {
    mComponents = new float[] { 0.0F, 0.0F, 0.0F, 1.0F };
    mColorSpace = ColorSpace.get(ColorSpace.Named.SRGB);
  }
  
  private Color(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this(paramFloat1, paramFloat2, paramFloat3, paramFloat4, ColorSpace.get(ColorSpace.Named.SRGB));
  }
  
  private Color(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, ColorSpace paramColorSpace)
  {
    mComponents = new float[] { paramFloat1, paramFloat2, paramFloat3, paramFloat4 };
    mColorSpace = paramColorSpace;
  }
  
  private Color(float[] paramArrayOfFloat, ColorSpace paramColorSpace)
  {
    mComponents = paramArrayOfFloat;
    mColorSpace = paramColorSpace;
  }
  
  public static int HSVToColor(int paramInt, float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length >= 3) {
      return nativeHSVToColor(paramInt, paramArrayOfFloat);
    }
    throw new RuntimeException("3 components required for hsv");
  }
  
  public static int HSVToColor(float[] paramArrayOfFloat)
  {
    return HSVToColor(255, paramArrayOfFloat);
  }
  
  public static void RGBToHSV(int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length >= 3)
    {
      nativeRGBToHSV(paramInt1, paramInt2, paramInt3, paramArrayOfFloat);
      return;
    }
    throw new RuntimeException("3 components required for hsv");
  }
  
  public static float alpha(long paramLong)
  {
    if ((0x3F & paramLong) == 0L) {
      return (float)(paramLong >> 56 & 0xFF) / 255.0F;
    }
    return (float)(paramLong >> 6 & 0x3FF) / 1023.0F;
  }
  
  public static int alpha(int paramInt)
  {
    return paramInt >>> 24;
  }
  
  public static int argb(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    int i = (int)(paramFloat1 * 255.0F + 0.5F);
    int j = (int)(paramFloat2 * 255.0F + 0.5F);
    int k = (int)(paramFloat3 * 255.0F + 0.5F);
    return (int)(255.0F * paramFloat4 + 0.5F) | i << 24 | j << 16 | k << 8;
  }
  
  public static int argb(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return paramInt1 << 24 | paramInt2 << 16 | paramInt3 << 8 | paramInt4;
  }
  
  public static float blue(long paramLong)
  {
    if ((0x3F & paramLong) == 0L) {
      return (float)(paramLong >> 32 & 0xFF) / 255.0F;
    }
    return Half.toFloat((short)(int)(paramLong >> 16 & 0xFFFF));
  }
  
  public static int blue(int paramInt)
  {
    return paramInt & 0xFF;
  }
  
  public static ColorSpace colorSpace(long paramLong)
  {
    return ColorSpace.get((int)(0x3F & paramLong));
  }
  
  public static void colorToHSV(int paramInt, float[] paramArrayOfFloat)
  {
    RGBToHSV(paramInt >> 16 & 0xFF, paramInt >> 8 & 0xFF, paramInt & 0xFF, paramArrayOfFloat);
  }
  
  public static long convert(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, ColorSpace.Connector paramConnector)
  {
    float[] arrayOfFloat = paramConnector.transform(paramFloat1, paramFloat2, paramFloat3);
    return pack(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], paramFloat4, paramConnector.getDestination());
  }
  
  public static long convert(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, ColorSpace paramColorSpace1, ColorSpace paramColorSpace2)
  {
    paramColorSpace1 = ColorSpace.connect(paramColorSpace1, paramColorSpace2).transform(paramFloat1, paramFloat2, paramFloat3);
    return pack(paramColorSpace1[0], paramColorSpace1[1], paramColorSpace1[2], paramFloat4, paramColorSpace2);
  }
  
  public static long convert(int paramInt, ColorSpace paramColorSpace)
  {
    return convert((paramInt >> 16 & 0xFF) / 255.0F, (paramInt >> 8 & 0xFF) / 255.0F, (paramInt & 0xFF) / 255.0F, (paramInt >> 24 & 0xFF) / 255.0F, ColorSpace.get(ColorSpace.Named.SRGB), paramColorSpace);
  }
  
  public static long convert(long paramLong, ColorSpace.Connector paramConnector)
  {
    return convert(red(paramLong), green(paramLong), blue(paramLong), alpha(paramLong), paramConnector);
  }
  
  public static long convert(long paramLong, ColorSpace paramColorSpace)
  {
    return convert(red(paramLong), green(paramLong), blue(paramLong), alpha(paramLong), colorSpace(paramLong), paramColorSpace);
  }
  
  public static int getHtmlColor(String paramString)
  {
    Integer localInteger = (Integer)sColorNameMap.get(paramString.toLowerCase(Locale.ROOT));
    if (localInteger != null) {
      return localInteger.intValue();
    }
    try
    {
      int i = XmlUtils.convertValueToInt(paramString, -1);
      return i;
    }
    catch (NumberFormatException paramString) {}
    return -1;
  }
  
  public static float green(long paramLong)
  {
    if ((0x3F & paramLong) == 0L) {
      return (float)(paramLong >> 40 & 0xFF) / 255.0F;
    }
    return Half.toFloat((short)(int)(paramLong >> 32 & 0xFFFF));
  }
  
  public static int green(int paramInt)
  {
    return paramInt >> 8 & 0xFF;
  }
  
  public static boolean isInColorSpace(long paramLong, ColorSpace paramColorSpace)
  {
    boolean bool;
    if ((int)(0x3F & paramLong) == paramColorSpace.getId()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isSrgb(long paramLong)
  {
    return colorSpace(paramLong).isSrgb();
  }
  
  public static boolean isWideGamut(long paramLong)
  {
    return colorSpace(paramLong).isWideGamut();
  }
  
  public static float luminance(int paramInt)
  {
    DoubleUnaryOperator localDoubleUnaryOperator = ((ColorSpace.Rgb)ColorSpace.get(ColorSpace.Named.SRGB)).getEotf();
    return (float)(0.2126D * localDoubleUnaryOperator.applyAsDouble(red(paramInt) / 255.0D) + 0.7152D * localDoubleUnaryOperator.applyAsDouble(green(paramInt) / 255.0D) + 0.0722D * localDoubleUnaryOperator.applyAsDouble(blue(paramInt) / 255.0D));
  }
  
  public static float luminance(long paramLong)
  {
    ColorSpace localColorSpace = colorSpace(paramLong);
    if (localColorSpace.getModel() == ColorSpace.Model.RGB)
    {
      localObject = ((ColorSpace.Rgb)localColorSpace).getEotf();
      return saturate((float)(0.2126D * ((DoubleUnaryOperator)localObject).applyAsDouble(red(paramLong)) + 0.7152D * ((DoubleUnaryOperator)localObject).applyAsDouble(green(paramLong)) + 0.0722D * ((DoubleUnaryOperator)localObject).applyAsDouble(blue(paramLong))));
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("The specified color must be encoded in an RGB color space. The supplied color space is ");
    ((StringBuilder)localObject).append(localColorSpace.getModel());
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  private static native int nativeHSVToColor(int paramInt, float[] paramArrayOfFloat);
  
  private static native void nativeRGBToHSV(int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat);
  
  public static long pack(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return pack(paramFloat1, paramFloat2, paramFloat3, 1.0F, ColorSpace.get(ColorSpace.Named.SRGB));
  }
  
  public static long pack(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return pack(paramFloat1, paramFloat2, paramFloat3, paramFloat4, ColorSpace.get(ColorSpace.Named.SRGB));
  }
  
  public static long pack(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, ColorSpace paramColorSpace)
  {
    int i;
    int j;
    if (paramColorSpace.isSrgb())
    {
      i = (int)(paramFloat4 * 255.0F + 0.5F);
      j = (int)(paramFloat1 * 255.0F + 0.5F);
      k = (int)(paramFloat2 * 255.0F + 0.5F);
      return (((int)(255.0F * paramFloat3 + 0.5F) | j << 16 | i << 24 | k << 8) & 0xFFFFFFFF) << 32;
    }
    int k = paramColorSpace.getId();
    if (k != -1)
    {
      if (paramColorSpace.getComponentCount() <= 3)
      {
        int m = Half.toHalf(paramFloat1);
        int n = Half.toHalf(paramFloat2);
        i = Half.toHalf(paramFloat3);
        j = (int)(Math.max(0.0F, Math.min(paramFloat4, 1.0F)) * 1023.0F + 0.5F);
        long l = m;
        return (n & 0xFFFF) << 32 | (l & 0xFFFF) << 48 | (i & 0xFFFF) << 16 | (j & 0x3FF) << 6 | k & 0x3F;
      }
      throw new IllegalArgumentException("The color space must use a color model with at most 3 components");
    }
    throw new IllegalArgumentException("Unknown color space, please use a color space returned by ColorSpace.get()");
  }
  
  public static long pack(int paramInt)
  {
    return (paramInt & 0xFFFFFFFF) << 32;
  }
  
  public static int parseColor(String paramString)
  {
    if (paramString.charAt(0) == '#')
    {
      long l = Long.parseLong(paramString.substring(1), 16);
      if (paramString.length() == 7) {
        l |= 0xFFFFFFFFFF000000;
      } else {
        if (paramString.length() != 9) {
          break label51;
        }
      }
      return (int)l;
      label51:
      throw new IllegalArgumentException("Unknown color");
    }
    paramString = (Integer)sColorNameMap.get(paramString.toLowerCase(Locale.ROOT));
    if (paramString != null) {
      return paramString.intValue();
    }
    throw new IllegalArgumentException("Unknown color");
  }
  
  public static float red(long paramLong)
  {
    if ((0x3F & paramLong) == 0L) {
      return (float)(paramLong >> 48 & 0xFF) / 255.0F;
    }
    return Half.toFloat((short)(int)(paramLong >> 48 & 0xFFFF));
  }
  
  public static int red(int paramInt)
  {
    return paramInt >> 16 & 0xFF;
  }
  
  public static int rgb(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    int i = (int)(paramFloat1 * 255.0F + 0.5F);
    int j = (int)(paramFloat2 * 255.0F + 0.5F);
    return (int)(255.0F * paramFloat3 + 0.5F) | i << 16 | 0xFF000000 | j << 8;
  }
  
  public static int rgb(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 << 16 | 0xFF000000 | paramInt2 << 8 | paramInt3;
  }
  
  private static float saturate(float paramFloat)
  {
    float f = 0.0F;
    if (paramFloat <= 0.0F) {
      paramFloat = f;
    } else if (paramFloat >= 1.0F) {
      paramFloat = 1.0F;
    }
    return paramFloat;
  }
  
  public static int toArgb(long paramLong)
  {
    if ((0x3F & paramLong) == 0L) {
      return (int)(paramLong >> 32);
    }
    float f1 = red(paramLong);
    float f2 = green(paramLong);
    float f3 = blue(paramLong);
    float f4 = alpha(paramLong);
    float[] arrayOfFloat = ColorSpace.connect(colorSpace(paramLong)).transform(f1, f2, f3);
    int i = (int)(f4 * 255.0F + 0.5F);
    int j = (int)(arrayOfFloat[0] * 255.0F + 0.5F);
    int k = (int)(arrayOfFloat[1] * 255.0F + 0.5F);
    return (int)(arrayOfFloat[2] * 255.0F + 0.5F) | i << 24 | j << 16 | k << 8;
  }
  
  public static Color valueOf(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return new Color(paramFloat1, paramFloat2, paramFloat3, 1.0F);
  }
  
  public static Color valueOf(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return new Color(saturate(paramFloat1), saturate(paramFloat2), saturate(paramFloat3), saturate(paramFloat4));
  }
  
  public static Color valueOf(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, ColorSpace paramColorSpace)
  {
    if (paramColorSpace.getComponentCount() <= 3) {
      return new Color(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramColorSpace);
    }
    throw new IllegalArgumentException("The specified color space must use a color model with at most 3 color components");
  }
  
  public static Color valueOf(int paramInt)
  {
    return new Color((paramInt >> 16 & 0xFF) / 255.0F, (paramInt >> 8 & 0xFF) / 255.0F, (paramInt & 0xFF) / 255.0F, (paramInt >> 24 & 0xFF) / 255.0F, ColorSpace.get(ColorSpace.Named.SRGB));
  }
  
  public static Color valueOf(long paramLong)
  {
    return new Color(red(paramLong), green(paramLong), blue(paramLong), alpha(paramLong), colorSpace(paramLong));
  }
  
  public static Color valueOf(float[] paramArrayOfFloat, ColorSpace paramColorSpace)
  {
    if (paramArrayOfFloat.length >= paramColorSpace.getComponentCount() + 1) {
      return new Color(Arrays.copyOf(paramArrayOfFloat, paramColorSpace.getComponentCount() + 1), paramColorSpace);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Received a component array of length ");
    localStringBuilder.append(paramArrayOfFloat.length);
    localStringBuilder.append(" but the color model requires ");
    localStringBuilder.append(paramColorSpace.getComponentCount() + 1);
    localStringBuilder.append(" (including alpha)");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public float alpha()
  {
    return mComponents[(mComponents.length - 1)];
  }
  
  public float blue()
  {
    return mComponents[2];
  }
  
  public Color convert(ColorSpace paramColorSpace)
  {
    ColorSpace.Connector localConnector = ColorSpace.connect(mColorSpace, paramColorSpace);
    float[] arrayOfFloat = new float[4];
    arrayOfFloat[0] = mComponents[0];
    arrayOfFloat[1] = mComponents[1];
    arrayOfFloat[2] = mComponents[2];
    arrayOfFloat[3] = mComponents[3];
    localConnector.transform(arrayOfFloat);
    return new Color(arrayOfFloat, paramColorSpace);
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (Color)paramObject;
      if (!Arrays.equals(mComponents, mComponents)) {
        return false;
      }
      return mColorSpace.equals(mColorSpace);
    }
    return false;
  }
  
  public ColorSpace getColorSpace()
  {
    return mColorSpace;
  }
  
  public float getComponent(int paramInt)
  {
    return mComponents[paramInt];
  }
  
  public int getComponentCount()
  {
    return mColorSpace.getComponentCount() + 1;
  }
  
  public float[] getComponents()
  {
    return Arrays.copyOf(mComponents, mComponents.length);
  }
  
  public float[] getComponents(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat == null) {
      return Arrays.copyOf(mComponents, mComponents.length);
    }
    if (paramArrayOfFloat.length >= mComponents.length)
    {
      System.arraycopy(mComponents, 0, paramArrayOfFloat, 0, mComponents.length);
      return paramArrayOfFloat;
    }
    paramArrayOfFloat = new StringBuilder();
    paramArrayOfFloat.append("The specified array's length must be at least ");
    paramArrayOfFloat.append(mComponents.length);
    throw new IllegalArgumentException(paramArrayOfFloat.toString());
  }
  
  public ColorSpace.Model getModel()
  {
    return mColorSpace.getModel();
  }
  
  public float green()
  {
    return mComponents[1];
  }
  
  public int hashCode()
  {
    return 31 * Arrays.hashCode(mComponents) + mColorSpace.hashCode();
  }
  
  public boolean isSrgb()
  {
    return getColorSpace().isSrgb();
  }
  
  public boolean isWideGamut()
  {
    return getColorSpace().isWideGamut();
  }
  
  public float luminance()
  {
    if (mColorSpace.getModel() == ColorSpace.Model.RGB)
    {
      localObject = ((ColorSpace.Rgb)mColorSpace).getEotf();
      return saturate((float)(0.2126D * ((DoubleUnaryOperator)localObject).applyAsDouble(mComponents[0]) + 0.7152D * ((DoubleUnaryOperator)localObject).applyAsDouble(mComponents[1]) + 0.0722D * ((DoubleUnaryOperator)localObject).applyAsDouble(mComponents[2])));
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("The specified color must be encoded in an RGB color space. The supplied color space is ");
    ((StringBuilder)localObject).append(mColorSpace.getModel());
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public long pack()
  {
    return pack(mComponents[0], mComponents[1], mComponents[2], mComponents[3], mColorSpace);
  }
  
  public float red()
  {
    return mComponents[0];
  }
  
  public int toArgb()
  {
    if (mColorSpace.isSrgb()) {
      return (int)(mComponents[3] * 255.0F + 0.5F) << 24 | (int)(mComponents[0] * 255.0F + 0.5F) << 16 | (int)(mComponents[1] * 255.0F + 0.5F) << 8 | (int)(mComponents[2] * 255.0F + 0.5F);
    }
    float[] arrayOfFloat = new float[4];
    arrayOfFloat[0] = mComponents[0];
    arrayOfFloat[1] = mComponents[1];
    arrayOfFloat[2] = mComponents[2];
    arrayOfFloat[3] = mComponents[3];
    ColorSpace.connect(mColorSpace).transform(arrayOfFloat);
    int i = (int)(arrayOfFloat[3] * 255.0F + 0.5F);
    int j = (int)(arrayOfFloat[0] * 255.0F + 0.5F);
    int k = (int)(arrayOfFloat[1] * 255.0F + 0.5F);
    return (int)(arrayOfFloat[2] * 255.0F + 0.5F) | k << 8 | j << 16 | i << 24;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("Color(");
    float[] arrayOfFloat = mComponents;
    int i = arrayOfFloat.length;
    for (int j = 0; j < i; j++)
    {
      localStringBuilder.append(arrayOfFloat[j]);
      localStringBuilder.append(", ");
    }
    localStringBuilder.append(mColorSpace.getName());
    localStringBuilder.append(')');
    return localStringBuilder.toString();
  }
}
