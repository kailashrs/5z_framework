package android.util;

class ContainerHelpers
{
  ContainerHelpers() {}
  
  static int binarySearch(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int i = 0;
    paramInt1--;
    while (i <= paramInt1)
    {
      int j = i + paramInt1 >>> 1;
      int k = paramArrayOfInt[j];
      if (k < paramInt2)
      {
        i = j + 1;
      }
      else
      {
        if (k <= paramInt2) {
          break label51;
        }
        paramInt1 = j - 1;
      }
      continue;
      label51:
      return j;
    }
    return i;
  }
  
  static int binarySearch(long[] paramArrayOfLong, int paramInt, long paramLong)
  {
    int i = 0;
    paramInt--;
    while (i <= paramInt)
    {
      int j = i + paramInt >>> 1;
      long l = paramArrayOfLong[j];
      if (l < paramLong)
      {
        i = j + 1;
      }
      else
      {
        if (l <= paramLong) {
          break label57;
        }
        paramInt = j - 1;
      }
      continue;
      label57:
      return j;
    }
    return i;
  }
}
