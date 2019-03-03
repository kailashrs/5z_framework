package android.util;

public class TypedValue
{
  public static final int COMPLEX_MANTISSA_MASK = 16777215;
  public static final int COMPLEX_MANTISSA_SHIFT = 8;
  public static final int COMPLEX_RADIX_0p23 = 3;
  public static final int COMPLEX_RADIX_16p7 = 1;
  public static final int COMPLEX_RADIX_23p0 = 0;
  public static final int COMPLEX_RADIX_8p15 = 2;
  public static final int COMPLEX_RADIX_MASK = 3;
  public static final int COMPLEX_RADIX_SHIFT = 4;
  public static final int COMPLEX_UNIT_DIP = 1;
  public static final int COMPLEX_UNIT_FRACTION = 0;
  public static final int COMPLEX_UNIT_FRACTION_PARENT = 1;
  public static final int COMPLEX_UNIT_IN = 4;
  public static final int COMPLEX_UNIT_MASK = 15;
  public static final int COMPLEX_UNIT_MM = 5;
  public static final int COMPLEX_UNIT_PT = 3;
  public static final int COMPLEX_UNIT_PX = 0;
  public static final int COMPLEX_UNIT_SHIFT = 0;
  public static final int COMPLEX_UNIT_SP = 2;
  public static final int DATA_NULL_EMPTY = 1;
  public static final int DATA_NULL_UNDEFINED = 0;
  public static final int DENSITY_DEFAULT = 0;
  public static final int DENSITY_NONE = 65535;
  private static final String[] DIMENSION_UNIT_STRS = { "px", "dip", "sp", "pt", "in", "mm" };
  private static final String[] FRACTION_UNIT_STRS = { "%", "%p" };
  private static final float MANTISSA_MULT = 0.00390625F;
  private static final float[] RADIX_MULTS = { 0.00390625F, 3.0517578E-5F, 1.1920929E-7F, 4.656613E-10F };
  public static final int TYPE_ATTRIBUTE = 2;
  public static final int TYPE_DIMENSION = 5;
  public static final int TYPE_FIRST_COLOR_INT = 28;
  public static final int TYPE_FIRST_INT = 16;
  public static final int TYPE_FLOAT = 4;
  public static final int TYPE_FRACTION = 6;
  public static final int TYPE_INT_BOOLEAN = 18;
  public static final int TYPE_INT_COLOR_ARGB4 = 30;
  public static final int TYPE_INT_COLOR_ARGB8 = 28;
  public static final int TYPE_INT_COLOR_RGB4 = 31;
  public static final int TYPE_INT_COLOR_RGB8 = 29;
  public static final int TYPE_INT_DEC = 16;
  public static final int TYPE_INT_HEX = 17;
  public static final int TYPE_LAST_COLOR_INT = 31;
  public static final int TYPE_LAST_INT = 31;
  public static final int TYPE_NULL = 0;
  public static final int TYPE_REFERENCE = 1;
  public static final int TYPE_STRING = 3;
  public int assetCookie;
  public int changingConfigurations = -1;
  public int data;
  public int density;
  public int resourceId;
  public CharSequence string;
  public int type;
  
  public TypedValue() {}
  
  public static float applyDimension(int paramInt, float paramFloat, DisplayMetrics paramDisplayMetrics)
  {
    switch (paramInt)
    {
    default: 
      return 0.0F;
    case 5: 
      return xdpi * paramFloat * 0.03937008F;
    case 4: 
      return xdpi * paramFloat;
    case 3: 
      return xdpi * paramFloat * 0.013888889F;
    case 2: 
      return scaledDensity * paramFloat;
    case 1: 
      return density * paramFloat;
    }
    return paramFloat;
  }
  
  public static final String coerceToString(int paramInt1, int paramInt2)
  {
    Object localObject;
    switch (paramInt1)
    {
    default: 
      if ((paramInt1 >= 28) && (paramInt1 <= 31))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("#");
        ((StringBuilder)localObject).append(Integer.toHexString(paramInt2));
        return ((StringBuilder)localObject).toString();
      }
      break;
    case 18: 
      if (paramInt2 != 0) {
        localObject = "true";
      } else {
        localObject = "false";
      }
      return localObject;
    case 17: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("0x");
      ((StringBuilder)localObject).append(Integer.toHexString(paramInt2));
      return ((StringBuilder)localObject).toString();
    case 6: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(Float.toString(complexToFloat(paramInt2) * 100.0F));
      ((StringBuilder)localObject).append(FRACTION_UNIT_STRS[(paramInt2 >> 0 & 0xF)]);
      return ((StringBuilder)localObject).toString();
    case 5: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(Float.toString(complexToFloat(paramInt2)));
      ((StringBuilder)localObject).append(DIMENSION_UNIT_STRS[(paramInt2 >> 0 & 0xF)]);
      return ((StringBuilder)localObject).toString();
    case 4: 
      return Float.toString(Float.intBitsToFloat(paramInt2));
    case 2: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("?");
      ((StringBuilder)localObject).append(paramInt2);
      return ((StringBuilder)localObject).toString();
    case 1: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("@");
      ((StringBuilder)localObject).append(paramInt2);
      return ((StringBuilder)localObject).toString();
    case 0: 
      return null;
    }
    if ((paramInt1 >= 16) && (paramInt1 <= 31)) {
      return Integer.toString(paramInt2);
    }
    return null;
  }
  
  public static float complexToDimension(int paramInt, DisplayMetrics paramDisplayMetrics)
  {
    return applyDimension(paramInt >> 0 & 0xF, complexToFloat(paramInt), paramDisplayMetrics);
  }
  
  @Deprecated
  public static float complexToDimensionNoisy(int paramInt, DisplayMetrics paramDisplayMetrics)
  {
    return complexToDimension(paramInt, paramDisplayMetrics);
  }
  
  public static int complexToDimensionPixelOffset(int paramInt, DisplayMetrics paramDisplayMetrics)
  {
    return (int)applyDimension(paramInt >> 0 & 0xF, complexToFloat(paramInt), paramDisplayMetrics);
  }
  
  public static int complexToDimensionPixelSize(int paramInt, DisplayMetrics paramDisplayMetrics)
  {
    float f1 = complexToFloat(paramInt);
    float f2 = applyDimension(paramInt >> 0 & 0xF, f1, paramDisplayMetrics);
    if (f2 >= 0.0F) {
      f2 = 0.5F + f2;
    } else {
      f2 -= 0.5F;
    }
    paramInt = (int)f2;
    if (paramInt != 0) {
      return paramInt;
    }
    if (f1 == 0.0F) {
      return 0;
    }
    if (f1 > 0.0F) {
      return 1;
    }
    return -1;
  }
  
  public static float complexToFloat(int paramInt)
  {
    return (paramInt & 0xFF00) * RADIX_MULTS[(paramInt >> 4 & 0x3)];
  }
  
  public static float complexToFraction(int paramInt, float paramFloat1, float paramFloat2)
  {
    switch (paramInt >> 0 & 0xF)
    {
    default: 
      return 0.0F;
    case 1: 
      return complexToFloat(paramInt) * paramFloat2;
    }
    return complexToFloat(paramInt) * paramFloat1;
  }
  
  public final CharSequence coerceToString()
  {
    int i = type;
    if (i == 3) {
      return string;
    }
    return coerceToString(i, data);
  }
  
  public int getComplexUnit()
  {
    return data >> 0 & 0xF;
  }
  
  public float getDimension(DisplayMetrics paramDisplayMetrics)
  {
    return complexToDimension(data, paramDisplayMetrics);
  }
  
  public final float getFloat()
  {
    return Float.intBitsToFloat(data);
  }
  
  public float getFraction(float paramFloat1, float paramFloat2)
  {
    return complexToFraction(data, paramFloat1, paramFloat2);
  }
  
  public void setTo(TypedValue paramTypedValue)
  {
    type = type;
    string = string;
    data = data;
    assetCookie = assetCookie;
    resourceId = resourceId;
    density = density;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("TypedValue{t=0x");
    localStringBuilder.append(Integer.toHexString(type));
    localStringBuilder.append("/d=0x");
    localStringBuilder.append(Integer.toHexString(data));
    if (type == 3)
    {
      localStringBuilder.append(" \"");
      Object localObject;
      if (string != null) {
        localObject = string;
      } else {
        localObject = "<null>";
      }
      localStringBuilder.append((CharSequence)localObject);
      localStringBuilder.append("\"");
    }
    if (assetCookie != 0)
    {
      localStringBuilder.append(" a=");
      localStringBuilder.append(assetCookie);
    }
    if (resourceId != 0)
    {
      localStringBuilder.append(" r=0x");
      localStringBuilder.append(Integer.toHexString(resourceId));
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
