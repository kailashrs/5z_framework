package android.animation;

public class FloatArrayEvaluator
  implements TypeEvaluator<float[]>
{
  private float[] mArray;
  
  public FloatArrayEvaluator() {}
  
  public FloatArrayEvaluator(float[] paramArrayOfFloat)
  {
    mArray = paramArrayOfFloat;
  }
  
  public float[] evaluate(float paramFloat, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float[] arrayOfFloat1 = mArray;
    float[] arrayOfFloat2 = arrayOfFloat1;
    if (arrayOfFloat1 == null) {
      arrayOfFloat2 = new float[paramArrayOfFloat1.length];
    }
    for (int i = 0; i < arrayOfFloat2.length; i++)
    {
      float f = paramArrayOfFloat1[i];
      arrayOfFloat2[i] = ((paramArrayOfFloat2[i] - f) * paramFloat + f);
    }
    return arrayOfFloat2;
  }
}
