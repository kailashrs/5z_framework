package android.gesture;

import android.graphics.RectF;
import android.util.Log;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public final class GestureUtils
{
  private static final float NONUNIFORM_SCALE = (float)Math.sqrt(2.0D);
  private static final float SCALING_THRESHOLD = 0.26F;
  
  private GestureUtils() {}
  
  static void closeStream(Closeable paramCloseable)
  {
    if (paramCloseable != null) {
      try
      {
        paramCloseable.close();
      }
      catch (IOException paramCloseable)
      {
        Log.e("Gestures", "Could not close stream", paramCloseable);
      }
    }
  }
  
  static float[] computeCentroid(float[] paramArrayOfFloat)
  {
    int i = paramArrayOfFloat.length;
    float f1 = 0.0F;
    float f2 = 0.0F;
    for (int j = 0; j < i; j++)
    {
      f2 += paramArrayOfFloat[j];
      j++;
      f1 += paramArrayOfFloat[j];
    }
    return new float[] { 2.0F * f2 / i, 2.0F * f1 / i };
  }
  
  private static float[][] computeCoVariance(float[] paramArrayOfFloat)
  {
    float[][] arrayOfFloat = new float[2][2];
    arrayOfFloat[0][0] = 0.0F;
    arrayOfFloat[0][1] = 0.0F;
    arrayOfFloat[1][0] = 0.0F;
    arrayOfFloat[1][1] = 0.0F;
    int i = paramArrayOfFloat.length;
    for (int j = 0; j < i; j++)
    {
      float f1 = paramArrayOfFloat[j];
      j++;
      float f2 = paramArrayOfFloat[j];
      float[] arrayOfFloat1 = arrayOfFloat[0];
      arrayOfFloat1[0] += f1 * f1;
      arrayOfFloat1 = arrayOfFloat[0];
      arrayOfFloat1[1] += f1 * f2;
      arrayOfFloat[1][0] = arrayOfFloat[0][1];
      arrayOfFloat1 = arrayOfFloat[1];
      arrayOfFloat1[1] += f2 * f2;
    }
    paramArrayOfFloat = arrayOfFloat[0];
    paramArrayOfFloat[0] /= i / 2;
    paramArrayOfFloat = arrayOfFloat[0];
    paramArrayOfFloat[1] /= i / 2;
    paramArrayOfFloat = arrayOfFloat[1];
    paramArrayOfFloat[0] /= i / 2;
    paramArrayOfFloat = arrayOfFloat[1];
    paramArrayOfFloat[1] /= i / 2;
    return arrayOfFloat;
  }
  
  private static float[] computeOrientation(float[][] paramArrayOfFloat)
  {
    float[] arrayOfFloat = new float[2];
    if ((paramArrayOfFloat[0][1] == 0.0F) || (paramArrayOfFloat[1][0] == 0.0F))
    {
      arrayOfFloat[0] = 1.0F;
      arrayOfFloat[1] = 0.0F;
    }
    float f1 = -paramArrayOfFloat[0][0];
    float f2 = paramArrayOfFloat[1][1];
    float f3 = paramArrayOfFloat[0][0];
    float f4 = paramArrayOfFloat[1][1];
    float f5 = paramArrayOfFloat[0][1];
    float f6 = paramArrayOfFloat[1][0];
    f2 = (f1 - f2) / 2.0F;
    f3 = (float)Math.sqrt(Math.pow(f2, 2.0D) - (f3 * f4 - f5 * f6));
    f4 = -f2 + f3;
    f3 = -f2 - f3;
    if (f4 == f3)
    {
      arrayOfFloat[0] = 0.0F;
      arrayOfFloat[1] = 0.0F;
    }
    else
    {
      if (f4 <= f3) {
        f4 = f3;
      }
      arrayOfFloat[0] = 1.0F;
      arrayOfFloat[1] = ((f4 - paramArrayOfFloat[0][0]) / paramArrayOfFloat[0][1]);
    }
    return arrayOfFloat;
  }
  
  public static OrientedBoundingBox computeOrientedBoundingBox(ArrayList<GesturePoint> paramArrayList)
  {
    int i = paramArrayList.size();
    float[] arrayOfFloat = new float[i * 2];
    for (int j = 0; j < i; j++)
    {
      GesturePoint localGesturePoint = (GesturePoint)paramArrayList.get(j);
      int k = j * 2;
      arrayOfFloat[k] = x;
      arrayOfFloat[(k + 1)] = y;
    }
    return computeOrientedBoundingBox(arrayOfFloat, computeCentroid(arrayOfFloat));
  }
  
  public static OrientedBoundingBox computeOrientedBoundingBox(float[] paramArrayOfFloat)
  {
    int i = paramArrayOfFloat.length;
    float[] arrayOfFloat = new float[i];
    for (int j = 0; j < i; j++) {
      arrayOfFloat[j] = paramArrayOfFloat[j];
    }
    return computeOrientedBoundingBox(arrayOfFloat, computeCentroid(arrayOfFloat));
  }
  
  private static OrientedBoundingBox computeOrientedBoundingBox(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    translate(paramArrayOfFloat1, -paramArrayOfFloat2[0], -paramArrayOfFloat2[1]);
    float[] arrayOfFloat = computeOrientation(computeCoVariance(paramArrayOfFloat1));
    float f1;
    if ((arrayOfFloat[0] == 0.0F) && (arrayOfFloat[1] == 0.0F))
    {
      f1 = -1.5707964F;
    }
    else
    {
      f1 = (float)Math.atan2(arrayOfFloat[1], arrayOfFloat[0]);
      rotate(paramArrayOfFloat1, -f1);
    }
    float f2 = Float.MIN_VALUE;
    float f3 = Float.MIN_VALUE;
    int i = paramArrayOfFloat1.length;
    float f4 = Float.MAX_VALUE;
    float f5 = Float.MAX_VALUE;
    int j = 0;
    while (j < i)
    {
      float f6 = f5;
      if (paramArrayOfFloat1[j] < f5) {
        f6 = paramArrayOfFloat1[j];
      }
      float f7 = f2;
      if (paramArrayOfFloat1[j] > f2) {
        f7 = paramArrayOfFloat1[j];
      }
      j++;
      float f8 = f4;
      if (paramArrayOfFloat1[j] < f4) {
        f8 = paramArrayOfFloat1[j];
      }
      f4 = f3;
      if (paramArrayOfFloat1[j] > f3) {
        f4 = paramArrayOfFloat1[j];
      }
      j++;
      f5 = f6;
      f2 = f7;
      f3 = f4;
      f4 = f8;
    }
    return new OrientedBoundingBox((float)(180.0F * f1 / 3.141592653589793D), paramArrayOfFloat2[0], paramArrayOfFloat2[1], f2 - f5, f3 - f4);
  }
  
  static float computeStraightness(float[] paramArrayOfFloat)
  {
    float f1 = computeTotalLength(paramArrayOfFloat);
    float f2 = paramArrayOfFloat[2];
    float f3 = paramArrayOfFloat[0];
    float f4 = paramArrayOfFloat[3];
    float f5 = paramArrayOfFloat[1];
    return (float)Math.hypot(f2 - f3, f4 - f5) / f1;
  }
  
  static float computeStraightness(float[] paramArrayOfFloat, float paramFloat)
  {
    float f1 = paramArrayOfFloat[2];
    float f2 = paramArrayOfFloat[0];
    float f3 = paramArrayOfFloat[3];
    float f4 = paramArrayOfFloat[1];
    return (float)Math.hypot(f1 - f2, f3 - f4) / paramFloat;
  }
  
  static float computeTotalLength(float[] paramArrayOfFloat)
  {
    float f1 = 0.0F;
    int i = paramArrayOfFloat.length;
    for (int j = 0; j < i - 4; j += 2)
    {
      float f2 = paramArrayOfFloat[(j + 2)];
      float f3 = paramArrayOfFloat[j];
      float f4 = paramArrayOfFloat[(j + 3)];
      float f5 = paramArrayOfFloat[(j + 1)];
      f1 = (float)(f1 + Math.hypot(f2 - f3, f4 - f5));
    }
    return f1;
  }
  
  static float cosineDistance(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f = 0.0F;
    int i = paramArrayOfFloat1.length;
    for (int j = 0; j < i; j++) {
      f += paramArrayOfFloat1[j] * paramArrayOfFloat2[j];
    }
    return (float)Math.acos(f);
  }
  
  static float minimumCosineDistance(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int paramInt)
  {
    int i = paramArrayOfFloat1.length;
    float f1 = 0.0F;
    float f2 = 0.0F;
    for (int j = 0; j < i; j += 2)
    {
      f1 += paramArrayOfFloat1[j] * paramArrayOfFloat2[j] + paramArrayOfFloat1[(j + 1)] * paramArrayOfFloat2[(j + 1)];
      f2 += paramArrayOfFloat1[j] * paramArrayOfFloat2[(j + 1)] - paramArrayOfFloat1[(j + 1)] * paramArrayOfFloat2[j];
    }
    if (f1 != 0.0F)
    {
      float f3 = f2 / f1;
      double d1 = Math.atan(f3);
      if ((paramInt > 2) && (Math.abs(d1) >= 3.141592653589793D / paramInt)) {
        return (float)Math.acos(f1);
      }
      d1 = Math.cos(d1);
      double d2 = f3;
      return (float)Math.acos(f1 * d1 + f2 * (d2 * d1));
    }
    return 1.5707964F;
  }
  
  private static void plot(float paramFloat1, float paramFloat2, float[] paramArrayOfFloat, int paramInt)
  {
    float f1 = 0.0F;
    if (paramFloat1 < 0.0F) {
      paramFloat1 = 0.0F;
    }
    if (paramFloat2 < 0.0F) {
      paramFloat2 = f1;
    }
    int i = (int)Math.floor(paramFloat1);
    int j = (int)Math.ceil(paramFloat1);
    int k = (int)Math.floor(paramFloat2);
    int m = (int)Math.ceil(paramFloat2);
    if ((paramFloat1 == i) && (paramFloat2 == k))
    {
      paramInt = m * paramInt + j;
      if (paramArrayOfFloat[paramInt] < 1.0F) {
        paramArrayOfFloat[paramInt] = 1.0F;
      }
    }
    else
    {
      double d1 = Math.pow(i - paramFloat1, 2.0D);
      double d2 = Math.pow(k - paramFloat2, 2.0D);
      double d3 = Math.pow(j - paramFloat1, 2.0D);
      double d4 = Math.pow(m - paramFloat2, 2.0D);
      float f2 = (float)Math.sqrt(d1 + d2);
      float f3 = (float)Math.sqrt(d3 + d2);
      f1 = (float)Math.sqrt(d1 + d4);
      paramFloat2 = (float)Math.sqrt(d3 + d4);
      paramFloat1 = f2 + f3 + f1 + paramFloat2;
      f2 /= paramFloat1;
      int n = k * paramInt + i;
      if (f2 > paramArrayOfFloat[n]) {
        paramArrayOfFloat[n] = f2;
      }
      f3 /= paramFloat1;
      k = k * paramInt + j;
      if (f3 > paramArrayOfFloat[k]) {
        paramArrayOfFloat[k] = f3;
      }
      f1 /= paramFloat1;
      i = m * paramInt + i;
      if (f1 > paramArrayOfFloat[i]) {
        paramArrayOfFloat[i] = f1;
      }
      paramFloat1 = paramFloat2 / paramFloat1;
      paramInt = m * paramInt + j;
      if (paramFloat1 > paramArrayOfFloat[paramInt]) {
        paramArrayOfFloat[paramInt] = paramFloat1;
      }
    }
  }
  
  static float[] rotate(float[] paramArrayOfFloat, float paramFloat)
  {
    float f1 = (float)Math.cos(paramFloat);
    float f2 = (float)Math.sin(paramFloat);
    int i = paramArrayOfFloat.length;
    for (int j = 0; j < i; j += 2)
    {
      float f3 = paramArrayOfFloat[j];
      float f4 = paramArrayOfFloat[(j + 1)];
      paramFloat = paramArrayOfFloat[j];
      float f5 = paramArrayOfFloat[(j + 1)];
      paramArrayOfFloat[j] = (f3 * f1 - f4 * f2);
      paramArrayOfFloat[(j + 1)] = (paramFloat * f2 + f5 * f1);
    }
    return paramArrayOfFloat;
  }
  
  static float[] scale(float[] paramArrayOfFloat, float paramFloat1, float paramFloat2)
  {
    int i = paramArrayOfFloat.length;
    for (int j = 0; j < i; j += 2)
    {
      paramArrayOfFloat[j] *= paramFloat1;
      int k = j + 1;
      paramArrayOfFloat[k] *= paramFloat2;
    }
    return paramArrayOfFloat;
  }
  
  public static float[] spatialSampling(Gesture paramGesture, int paramInt)
  {
    return spatialSampling(paramGesture, paramInt, false);
  }
  
  public static float[] spatialSampling(Gesture paramGesture, int paramInt, boolean paramBoolean)
  {
    float f1 = paramInt - 1;
    float[] arrayOfFloat = new float[paramInt * paramInt];
    Arrays.fill(arrayOfFloat, 0.0F);
    RectF localRectF = paramGesture.getBoundingBox();
    float f2 = localRectF.width();
    float f3 = localRectF.height();
    float f4 = f1 / f2;
    float f5 = f1 / f3;
    float f7;
    if (paramBoolean)
    {
      if (f4 < f5) {
        f6 = f4;
      } else {
        f6 = f5;
      }
      f7 = f6;
      f4 = f6;
      f6 = f7;
      f7 = f4;
    }
    else
    {
      f7 = f2 / f3;
      f6 = f7;
      if (f7 > 1.0F) {
        f6 = 1.0F / f7;
      }
      if (f6 < 0.26F)
      {
        if (f4 < f5) {
          f6 = f4;
        } else {
          f6 = f5;
        }
        f4 = f6;
        f7 = f6;
        f6 = f4;
      }
      else if (f4 > f5)
      {
        f7 = NONUNIFORM_SCALE * f5;
        f6 = f4;
        if (f7 < f4) {
          f6 = f7;
        }
        f7 = f5;
      }
      else
      {
        f8 = NONUNIFORM_SCALE * f4;
        f6 = f4;
        f7 = f5;
        if (f8 < f5)
        {
          f7 = f8;
          f6 = f4;
        }
      }
    }
    float f9 = -localRectF.centerX();
    float f10 = -localRectF.centerY();
    float f11 = f1 / 2.0F;
    float f12 = f1 / 2.0F;
    ArrayList localArrayList = paramGesture.getStrokes();
    int i = localArrayList.size();
    int j = 0;
    float f8 = f6;
    float f6 = f1;
    while (j < i)
    {
      Object localObject = getpoints;
      int k = localObject.length;
      paramGesture = new float[k];
      for (int m = 0; m < k; m += 2)
      {
        paramGesture[m] = ((localObject[m] + f9) * f8 + f11);
        paramGesture[(m + 1)] = ((localObject[(m + 1)] + f10) * f7 + f12);
      }
      float f13 = -1.0F;
      int n = 0;
      float f14 = -1.0F;
      m = k;
      while (n < m)
      {
        if (paramGesture[n] < 0.0F) {
          f5 = 0.0F;
        } else {
          f5 = paramGesture[n];
        }
        if (paramGesture[(n + 1)] < 0.0F) {
          f1 = 0.0F;
        } else {
          f1 = paramGesture[(n + 1)];
        }
        f4 = f5;
        if (f5 > f6) {
          f4 = f6;
        }
        if (f1 > f6) {
          f5 = f6;
        } else {
          f5 = f1;
        }
        plot(f4, f5, arrayOfFloat, paramInt);
        if (f13 != -1.0F)
        {
          float f15;
          if (f13 > f4)
          {
            f1 = (float)Math.ceil(f4);
            f15 = (f14 - f5) / (f13 - f4);
            while (f1 < f13)
            {
              plot(f1, (f1 - f4) * f15 + f5, arrayOfFloat, paramInt);
              f1 += 1.0F;
            }
          }
          else
          {
            localObject = paramGesture;
            paramGesture = (Gesture)localObject;
            if (f13 < f4)
            {
              f1 = (float)Math.ceil(f13);
              f15 = (f14 - f5) / (f13 - f4);
              for (;;)
              {
                paramGesture = (Gesture)localObject;
                if (f1 >= f4) {
                  break;
                }
                plot(f1, (f1 - f4) * f15 + f5, arrayOfFloat, paramInt);
                f1 += 1.0F;
              }
            }
          }
          if (f14 > f5)
          {
            f1 = (float)Math.ceil(f5);
            f13 = (f13 - f4) / (f14 - f5);
            while (f1 < f14)
            {
              plot((f1 - f5) * f13 + f4, f1, arrayOfFloat, paramInt);
              f1 += 1.0F;
            }
          }
          else if (f14 < f5)
          {
            f1 = (float)Math.ceil(f14);
            f14 = (f13 - f4) / (f14 - f5);
            while (f1 < f5)
            {
              plot((f1 - f5) * f14 + f4, f1, arrayOfFloat, paramInt);
              f1 += 1.0F;
            }
          }
        }
        n += 2;
        f13 = f4;
        f14 = f5;
      }
      j++;
    }
    return arrayOfFloat;
  }
  
  static float squaredEuclideanDistance(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f1 = 0.0F;
    int i = paramArrayOfFloat1.length;
    for (int j = 0; j < i; j++)
    {
      float f2 = paramArrayOfFloat1[j] - paramArrayOfFloat2[j];
      f1 += f2 * f2;
    }
    return f1 / i;
  }
  
  public static float[] temporalSampling(GestureStroke paramGestureStroke, int paramInt)
  {
    float f1 = length / (paramInt - 1);
    int i = paramInt * 2;
    float[] arrayOfFloat = new float[i];
    float f2 = 0.0F;
    paramGestureStroke = points;
    float f3 = paramGestureStroke[0];
    int j = 1;
    float f4 = paramGestureStroke[1];
    float f5 = Float.MIN_VALUE;
    float f6 = Float.MIN_VALUE;
    arrayOfFloat[0] = f3;
    paramInt = 0 + 1;
    arrayOfFloat[paramInt] = f4;
    int k = paramInt + 1;
    int m = 0;
    int n = paramGestureStroke.length / 2;
    while (m < n)
    {
      float f7 = f5;
      paramInt = m;
      if (f5 == Float.MIN_VALUE)
      {
        paramInt = m + 1;
        if (paramInt >= n) {
          break;
        }
        f7 = paramGestureStroke[(paramInt * 2)];
        f6 = paramGestureStroke[(paramInt * 2 + j)];
      }
      f5 = f7 - f3;
      float f8 = f6 - f4;
      float f9 = (float)Math.hypot(f5, f8);
      if (f2 + f9 >= f1)
      {
        f2 = (f1 - f2) / f9;
        f5 = f2 * f5 + f3;
        f3 = f2 * f8 + f4;
        arrayOfFloat[k] = f5;
        k++;
        arrayOfFloat[k] = f3;
        k++;
        f4 = f5;
        f5 = f3;
        f3 = 0.0F;
      }
      else
      {
        f3 = f2 + f9;
        f8 = Float.MIN_VALUE;
        f2 = Float.MIN_VALUE;
        f5 = f6;
        f4 = f7;
        f6 = f8;
        f7 = f2;
      }
      j = 1;
      f2 = f3;
      f3 = f4;
      f4 = f5;
      f5 = f7;
      m = paramInt;
    }
    while (k < i)
    {
      arrayOfFloat[k] = f3;
      arrayOfFloat[(k + 1)] = f4;
      k += 2;
    }
    return arrayOfFloat;
  }
  
  static float[] translate(float[] paramArrayOfFloat, float paramFloat1, float paramFloat2)
  {
    int i = paramArrayOfFloat.length;
    for (int j = 0; j < i; j += 2)
    {
      paramArrayOfFloat[j] += paramFloat1;
      int k = j + 1;
      paramArrayOfFloat[k] += paramFloat2;
    }
    return paramArrayOfFloat;
  }
}
