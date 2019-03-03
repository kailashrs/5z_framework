package android.util;

public abstract class Spline
{
  public Spline() {}
  
  public static Spline createLinearSpline(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    return new LinearSpline(paramArrayOfFloat1, paramArrayOfFloat2);
  }
  
  public static Spline createMonotoneCubicSpline(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    return new MonotoneCubicSpline(paramArrayOfFloat1, paramArrayOfFloat2);
  }
  
  public static Spline createSpline(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if (isStrictlyIncreasing(paramArrayOfFloat1))
    {
      if (isMonotonic(paramArrayOfFloat2)) {
        return createMonotoneCubicSpline(paramArrayOfFloat1, paramArrayOfFloat2);
      }
      return createLinearSpline(paramArrayOfFloat1, paramArrayOfFloat2);
    }
    throw new IllegalArgumentException("The control points must all have strictly increasing X values.");
  }
  
  private static boolean isMonotonic(float[] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat != null) && (paramArrayOfFloat.length >= 2))
    {
      float f1 = paramArrayOfFloat[0];
      for (int i = 1; i < paramArrayOfFloat.length; i++)
      {
        float f2 = paramArrayOfFloat[i];
        if (f2 < f1) {
          return false;
        }
        f1 = f2;
      }
      return true;
    }
    throw new IllegalArgumentException("There must be at least two control points.");
  }
  
  private static boolean isStrictlyIncreasing(float[] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat != null) && (paramArrayOfFloat.length >= 2))
    {
      float f1 = paramArrayOfFloat[0];
      for (int i = 1; i < paramArrayOfFloat.length; i++)
      {
        float f2 = paramArrayOfFloat[i];
        if (f2 <= f1) {
          return false;
        }
        f1 = f2;
      }
      return true;
    }
    throw new IllegalArgumentException("There must be at least two control points.");
  }
  
  public abstract float interpolate(float paramFloat);
  
  public static class LinearSpline
    extends Spline
  {
    private final float[] mM;
    private final float[] mX;
    private final float[] mY;
    
    public LinearSpline(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
    {
      if ((paramArrayOfFloat1 != null) && (paramArrayOfFloat2 != null) && (paramArrayOfFloat1.length == paramArrayOfFloat2.length) && (paramArrayOfFloat1.length >= 2))
      {
        int i = paramArrayOfFloat1.length;
        mM = new float[i - 1];
        for (int j = 0; j < i - 1; j++) {
          mM[j] = ((paramArrayOfFloat2[(j + 1)] - paramArrayOfFloat2[j]) / (paramArrayOfFloat1[(j + 1)] - paramArrayOfFloat1[j]));
        }
        mX = paramArrayOfFloat1;
        mY = paramArrayOfFloat2;
        return;
      }
      throw new IllegalArgumentException("There must be at least two control points and the arrays must be of equal length.");
    }
    
    public float interpolate(float paramFloat)
    {
      int i = mX.length;
      if (Float.isNaN(paramFloat)) {
        return paramFloat;
      }
      float[] arrayOfFloat = mX;
      int j = 0;
      if (paramFloat <= arrayOfFloat[0]) {
        return mY[0];
      }
      if (paramFloat >= mX[(i - 1)]) {
        return mY[(i - 1)];
      }
      while (paramFloat >= mX[(j + 1)])
      {
        i = j + 1;
        j = i;
        if (paramFloat == mX[i]) {
          return mY[i];
        }
      }
      return mY[j] + mM[j] * (paramFloat - mX[j]);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = mX.length;
      localStringBuilder.append("LinearSpline{[");
      for (int j = 0; j < i; j++)
      {
        if (j != 0) {
          localStringBuilder.append(", ");
        }
        localStringBuilder.append("(");
        localStringBuilder.append(mX[j]);
        localStringBuilder.append(", ");
        localStringBuilder.append(mY[j]);
        if (j < i - 1)
        {
          localStringBuilder.append(": ");
          localStringBuilder.append(mM[j]);
        }
        localStringBuilder.append(")");
      }
      localStringBuilder.append("]}");
      return localStringBuilder.toString();
    }
  }
  
  public static class MonotoneCubicSpline
    extends Spline
  {
    private float[] mM;
    private float[] mX;
    private float[] mY;
    
    public MonotoneCubicSpline(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
    {
      if ((paramArrayOfFloat1 != null) && (paramArrayOfFloat2 != null) && (paramArrayOfFloat1.length == paramArrayOfFloat2.length) && (paramArrayOfFloat1.length >= 2))
      {
        int i = paramArrayOfFloat1.length;
        float[] arrayOfFloat1 = new float[i - 1];
        float[] arrayOfFloat2 = new float[i];
        int j = 0;
        int k = 0;
        float f1;
        while (k < i - 1)
        {
          f1 = paramArrayOfFloat1[(k + 1)] - paramArrayOfFloat1[k];
          if (f1 > 0.0F)
          {
            arrayOfFloat1[k] = ((paramArrayOfFloat2[(k + 1)] - paramArrayOfFloat2[k]) / f1);
            k++;
          }
          else
          {
            throw new IllegalArgumentException("The control points must all have strictly increasing X values.");
          }
        }
        arrayOfFloat2[0] = arrayOfFloat1[0];
        for (k = 1; k < i - 1; k++) {
          arrayOfFloat2[k] = ((arrayOfFloat1[(k - 1)] + arrayOfFloat1[k]) * 0.5F);
        }
        arrayOfFloat2[(i - 1)] = arrayOfFloat1[(i - 2)];
        k = j;
        while (k < i - 1)
        {
          if (arrayOfFloat1[k] == 0.0F)
          {
            arrayOfFloat2[k] = 0.0F;
            arrayOfFloat2[(k + 1)] = 0.0F;
          }
          else
          {
            f1 = arrayOfFloat2[k] / arrayOfFloat1[k];
            float f2 = arrayOfFloat2[(k + 1)] / arrayOfFloat1[k];
            if ((f1 < 0.0F) || (f2 < 0.0F)) {
              break label313;
            }
            f1 = (float)Math.hypot(f1, f2);
            if (f1 > 3.0F)
            {
              f1 = 3.0F / f1;
              arrayOfFloat2[k] *= f1;
              j = k + 1;
              arrayOfFloat2[j] *= f1;
            }
          }
          k++;
          continue;
          label313:
          throw new IllegalArgumentException("The control points must have monotonic Y values.");
        }
        mX = paramArrayOfFloat1;
        mY = paramArrayOfFloat2;
        mM = arrayOfFloat2;
        return;
      }
      throw new IllegalArgumentException("There must be at least two control points and the arrays must be of equal length.");
    }
    
    public float interpolate(float paramFloat)
    {
      int i = mX.length;
      if (Float.isNaN(paramFloat)) {
        return paramFloat;
      }
      float[] arrayOfFloat = mX;
      int j = 0;
      if (paramFloat <= arrayOfFloat[0]) {
        return mY[0];
      }
      if (paramFloat >= mX[(i - 1)]) {
        return mY[(i - 1)];
      }
      while (paramFloat >= mX[(j + 1)])
      {
        i = j + 1;
        j = i;
        if (paramFloat == mX[i]) {
          return mY[i];
        }
      }
      float f = mX[(j + 1)] - mX[j];
      paramFloat = (paramFloat - mX[j]) / f;
      return (mY[j] * (2.0F * paramFloat + 1.0F) + mM[j] * f * paramFloat) * (1.0F - paramFloat) * (1.0F - paramFloat) + (mY[(j + 1)] * (3.0F - 2.0F * paramFloat) + mM[(j + 1)] * f * (paramFloat - 1.0F)) * paramFloat * paramFloat;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = mX.length;
      localStringBuilder.append("MonotoneCubicSpline{[");
      for (int j = 0; j < i; j++)
      {
        if (j != 0) {
          localStringBuilder.append(", ");
        }
        localStringBuilder.append("(");
        localStringBuilder.append(mX[j]);
        localStringBuilder.append(", ");
        localStringBuilder.append(mY[j]);
        localStringBuilder.append(": ");
        localStringBuilder.append(mM[j]);
        localStringBuilder.append(")");
      }
      localStringBuilder.append("]}");
      return localStringBuilder.toString();
    }
  }
}
