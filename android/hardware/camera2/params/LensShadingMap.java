package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import com.android.internal.util.Preconditions;
import java.util.Arrays;

public final class LensShadingMap
{
  public static final float MINIMUM_GAIN_FACTOR = 1.0F;
  private final int mColumns;
  private final float[] mElements;
  private final int mRows;
  
  public LensShadingMap(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    mRows = Preconditions.checkArgumentPositive(paramInt1, "rows must be positive");
    mColumns = Preconditions.checkArgumentPositive(paramInt2, "columns must be positive");
    mElements = ((float[])Preconditions.checkNotNull(paramArrayOfFloat, "elements must not be null"));
    if (paramArrayOfFloat.length == getGainFactorCount())
    {
      Preconditions.checkArrayElementsInRange(paramArrayOfFloat, 1.0F, Float.MAX_VALUE, "elements");
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("elements must be ");
    localStringBuilder.append(getGainFactorCount());
    localStringBuilder.append(" length, received ");
    localStringBuilder.append(paramArrayOfFloat.length);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void copyGainFactors(float[] paramArrayOfFloat, int paramInt)
  {
    Preconditions.checkArgumentNonnegative(paramInt, "offset must not be negative");
    Preconditions.checkNotNull(paramArrayOfFloat, "destination must not be null");
    if (paramArrayOfFloat.length + paramInt >= getGainFactorCount())
    {
      System.arraycopy(mElements, 0, paramArrayOfFloat, paramInt, getGainFactorCount());
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
    if ((paramObject instanceof LensShadingMap))
    {
      paramObject = (LensShadingMap)paramObject;
      if ((mRows == mRows) && (mColumns == mColumns) && (Arrays.equals(mElements, mElements))) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public int getColumnCount()
  {
    return mColumns;
  }
  
  public float getGainFactor(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 4))
    {
      if ((paramInt2 >= 0) && (paramInt2 < mColumns))
      {
        if ((paramInt3 >= 0) && (paramInt3 < mRows)) {
          return mElements[((mColumns * paramInt3 + paramInt2) * 4 + paramInt1)];
        }
        throw new IllegalArgumentException("row out of range");
      }
      throw new IllegalArgumentException("column out of range");
    }
    throw new IllegalArgumentException("colorChannel out of range");
  }
  
  public int getGainFactorCount()
  {
    return mRows * mColumns * 4;
  }
  
  public RggbChannelVector getGainFactorVector(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < mColumns))
    {
      if ((paramInt2 >= 0) && (paramInt2 < mRows))
      {
        paramInt1 = (mColumns * paramInt2 + paramInt1) * 4;
        return new RggbChannelVector(mElements[(0 + paramInt1)], mElements[(1 + paramInt1)], mElements[(2 + paramInt1)], mElements[(3 + paramInt1)]);
      }
      throw new IllegalArgumentException("row out of range");
    }
    throw new IllegalArgumentException("column out of range");
  }
  
  public int getRowCount()
  {
    return mRows;
  }
  
  public int hashCode()
  {
    int i = HashCodeHelpers.hashCode(mElements);
    return HashCodeHelpers.hashCode(new int[] { mRows, mColumns, i });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("LensShadingMap{");
    for (int i = 0; i < 4; i++)
    {
      localStringBuilder.append(new String[] { "R:(", "G_even:(", "G_odd:(", "B:(" }[i]);
      for (int j = 0; j < mRows; j++)
      {
        localStringBuilder.append("[");
        for (int k = 0; k < mColumns; k++)
        {
          localStringBuilder.append(getGainFactor(i, k, j));
          if (k < mColumns - 1) {
            localStringBuilder.append(", ");
          }
        }
        localStringBuilder.append("]");
        if (j < mRows - 1) {
          localStringBuilder.append(", ");
        }
      }
      localStringBuilder.append(")");
      if (i < 3) {
        localStringBuilder.append(", ");
      }
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
