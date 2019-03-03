package android.hardware.camera2.params;

import android.graphics.PointF;
import android.hardware.camera2.utils.HashCodeHelpers;
import com.android.internal.util.Preconditions;
import java.util.Arrays;

public final class TonemapCurve
{
  public static final int CHANNEL_BLUE = 2;
  public static final int CHANNEL_GREEN = 1;
  public static final int CHANNEL_RED = 0;
  public static final float LEVEL_BLACK = 0.0F;
  public static final float LEVEL_WHITE = 1.0F;
  private static final int MIN_CURVE_LENGTH = 4;
  private static final int OFFSET_POINT_IN = 0;
  private static final int OFFSET_POINT_OUT = 1;
  public static final int POINT_SIZE = 2;
  private static final int TONEMAP_MIN_CURVE_POINTS = 2;
  private final float[] mBlue;
  private final float[] mGreen;
  private boolean mHashCalculated = false;
  private int mHashCode;
  private final float[] mRed;
  
  public TonemapCurve(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3)
  {
    Preconditions.checkNotNull(paramArrayOfFloat1, "red must not be null");
    Preconditions.checkNotNull(paramArrayOfFloat2, "green must not be null");
    Preconditions.checkNotNull(paramArrayOfFloat3, "blue must not be null");
    checkArgumentArrayLengthDivisibleBy(paramArrayOfFloat1, 2, "red");
    checkArgumentArrayLengthDivisibleBy(paramArrayOfFloat2, 2, "green");
    checkArgumentArrayLengthDivisibleBy(paramArrayOfFloat3, 2, "blue");
    checkArgumentArrayLengthNoLessThan(paramArrayOfFloat1, 4, "red");
    checkArgumentArrayLengthNoLessThan(paramArrayOfFloat2, 4, "green");
    checkArgumentArrayLengthNoLessThan(paramArrayOfFloat3, 4, "blue");
    Preconditions.checkArrayElementsInRange(paramArrayOfFloat1, 0.0F, 1.0F, "red");
    Preconditions.checkArrayElementsInRange(paramArrayOfFloat2, 0.0F, 1.0F, "green");
    Preconditions.checkArrayElementsInRange(paramArrayOfFloat3, 0.0F, 1.0F, "blue");
    mRed = Arrays.copyOf(paramArrayOfFloat1, paramArrayOfFloat1.length);
    mGreen = Arrays.copyOf(paramArrayOfFloat2, paramArrayOfFloat2.length);
    mBlue = Arrays.copyOf(paramArrayOfFloat3, paramArrayOfFloat3.length);
  }
  
  private static void checkArgumentArrayLengthDivisibleBy(float[] paramArrayOfFloat, int paramInt, String paramString)
  {
    if (paramArrayOfFloat.length % paramInt == 0) {
      return;
    }
    paramArrayOfFloat = new StringBuilder();
    paramArrayOfFloat.append(paramString);
    paramArrayOfFloat.append(" size must be divisible by ");
    paramArrayOfFloat.append(paramInt);
    throw new IllegalArgumentException(paramArrayOfFloat.toString());
  }
  
  private static void checkArgumentArrayLengthNoLessThan(float[] paramArrayOfFloat, int paramInt, String paramString)
  {
    if (paramArrayOfFloat.length >= paramInt) {
      return;
    }
    paramArrayOfFloat = new StringBuilder();
    paramArrayOfFloat.append(paramString);
    paramArrayOfFloat.append(" size must be at least ");
    paramArrayOfFloat.append(paramInt);
    throw new IllegalArgumentException(paramArrayOfFloat.toString());
  }
  
  private static int checkArgumentColorChannel(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("colorChannel out of range");
    }
    return paramInt;
  }
  
  private String curveToString(int paramInt)
  {
    checkArgumentColorChannel(paramInt);
    StringBuilder localStringBuilder = new StringBuilder("[");
    float[] arrayOfFloat = getCurve(paramInt);
    int i = arrayOfFloat.length / 2;
    int j = 0;
    for (paramInt = 0; j < i; paramInt += 2)
    {
      localStringBuilder.append("(");
      localStringBuilder.append(arrayOfFloat[paramInt]);
      localStringBuilder.append(", ");
      localStringBuilder.append(arrayOfFloat[(paramInt + 1)]);
      localStringBuilder.append("), ");
      j++;
    }
    localStringBuilder.setLength(localStringBuilder.length() - 2);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  private float[] getCurve(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new AssertionError("colorChannel out of range");
    case 2: 
      return mBlue;
    case 1: 
      return mGreen;
    }
    return mRed;
  }
  
  public void copyColorCurve(int paramInt1, float[] paramArrayOfFloat, int paramInt2)
  {
    Preconditions.checkArgumentNonnegative(paramInt2, "offset must not be negative");
    Preconditions.checkNotNull(paramArrayOfFloat, "destination must not be null");
    if (paramArrayOfFloat.length + paramInt2 >= getPointCount(paramInt1) * 2)
    {
      float[] arrayOfFloat = getCurve(paramInt1);
      System.arraycopy(arrayOfFloat, 0, paramArrayOfFloat, paramInt2, arrayOfFloat.length);
      return;
    }
    throw new ArrayIndexOutOfBoundsException("destination too small to fit elements");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof TonemapCurve))
    {
      paramObject = (TonemapCurve)paramObject;
      if ((Arrays.equals(mRed, mRed)) && (Arrays.equals(mGreen, mGreen)) && (Arrays.equals(mBlue, mBlue))) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public PointF getPoint(int paramInt1, int paramInt2)
  {
    checkArgumentColorChannel(paramInt1);
    if ((paramInt2 >= 0) && (paramInt2 < getPointCount(paramInt1)))
    {
      float[] arrayOfFloat = getCurve(paramInt1);
      return new PointF(arrayOfFloat[(paramInt2 * 2 + 0)], arrayOfFloat[(paramInt2 * 2 + 1)]);
    }
    throw new IllegalArgumentException("index out of range");
  }
  
  public int getPointCount(int paramInt)
  {
    checkArgumentColorChannel(paramInt);
    return getCurve(paramInt).length / 2;
  }
  
  public int hashCode()
  {
    if (mHashCalculated) {
      return mHashCode;
    }
    mHashCode = HashCodeHelpers.hashCodeGeneric(new float[][] { mRed, mGreen, mBlue });
    mHashCalculated = true;
    return mHashCode;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("TonemapCurve{");
    localStringBuilder.append("R:");
    localStringBuilder.append(curveToString(0));
    localStringBuilder.append(", G:");
    localStringBuilder.append(curveToString(1));
    localStringBuilder.append(", B:");
    localStringBuilder.append(curveToString(2));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
