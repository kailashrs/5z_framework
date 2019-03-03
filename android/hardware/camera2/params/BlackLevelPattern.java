package android.hardware.camera2.params;

import com.android.internal.util.Preconditions;
import java.util.Arrays;

public final class BlackLevelPattern
{
  public static final int COUNT = 4;
  private final int[] mCfaOffsets;
  
  public BlackLevelPattern(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt != null)
    {
      if (paramArrayOfInt.length >= 4)
      {
        mCfaOffsets = Arrays.copyOf(paramArrayOfInt, 4);
        return;
      }
      throw new IllegalArgumentException("Invalid offsets array length");
    }
    throw new NullPointerException("Null offsets array passed to constructor");
  }
  
  public void copyTo(int[] paramArrayOfInt, int paramInt)
  {
    Preconditions.checkNotNull(paramArrayOfInt, "destination must not be null");
    if (paramInt >= 0)
    {
      if (paramArrayOfInt.length - paramInt >= 4)
      {
        for (int i = 0; i < 4; i++) {
          paramArrayOfInt[(paramInt + i)] = mCfaOffsets[i];
        }
        return;
      }
      throw new ArrayIndexOutOfBoundsException("destination too small to fit elements");
    }
    throw new IllegalArgumentException("Null offset passed to copyTo");
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof BlackLevelPattern)) {
      return Arrays.equals(mCfaOffsets, mCfaOffsets);
    }
    return false;
  }
  
  public int getOffsetForIndex(int paramInt1, int paramInt2)
  {
    if ((paramInt2 >= 0) && (paramInt1 >= 0)) {
      return mCfaOffsets[((paramInt2 & 0x1) << 1 | paramInt1 & 0x1)];
    }
    throw new IllegalArgumentException("column, row arguments must be positive");
  }
  
  public int hashCode()
  {
    return Arrays.hashCode(mCfaOffsets);
  }
  
  public String toString()
  {
    return String.format("BlackLevelPattern([%d, %d], [%d, %d])", new Object[] { Integer.valueOf(mCfaOffsets[0]), Integer.valueOf(mCfaOffsets[1]), Integer.valueOf(mCfaOffsets[2]), Integer.valueOf(mCfaOffsets[3]) });
  }
}
