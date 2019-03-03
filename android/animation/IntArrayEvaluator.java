package android.animation;

public class IntArrayEvaluator
  implements TypeEvaluator<int[]>
{
  private int[] mArray;
  
  public IntArrayEvaluator() {}
  
  public IntArrayEvaluator(int[] paramArrayOfInt)
  {
    mArray = paramArrayOfInt;
  }
  
  public int[] evaluate(float paramFloat, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int[] arrayOfInt1 = mArray;
    int[] arrayOfInt2 = arrayOfInt1;
    if (arrayOfInt1 == null) {
      arrayOfInt2 = new int[paramArrayOfInt1.length];
    }
    for (int i = 0; i < arrayOfInt2.length; i++)
    {
      int j = paramArrayOfInt1[i];
      int k = paramArrayOfInt2[i];
      arrayOfInt2[i] = ((int)(j + (k - j) * paramFloat));
    }
    return arrayOfInt2;
  }
}
