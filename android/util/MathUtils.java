package android.util;

public final class MathUtils
{
  private static final float DEG_TO_RAD = 0.017453292F;
  private static final float RAD_TO_DEG = 57.295784F;
  
  private MathUtils() {}
  
  public static float abs(float paramFloat)
  {
    if (paramFloat <= 0.0F) {
      paramFloat = -paramFloat;
    }
    return paramFloat;
  }
  
  public static float acos(float paramFloat)
  {
    return (float)Math.acos(paramFloat);
  }
  
  public static int addOrThrow(int paramInt1, int paramInt2)
    throws IllegalArgumentException
  {
    if (paramInt2 == 0) {
      return paramInt1;
    }
    if ((paramInt2 > 0) && (paramInt1 <= Integer.MAX_VALUE - paramInt2)) {
      return paramInt1 + paramInt2;
    }
    if ((paramInt2 < 0) && (paramInt1 >= Integer.MIN_VALUE - paramInt2)) {
      return paramInt1 + paramInt2;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Addition overflow: ");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" + ");
    localStringBuilder.append(paramInt2);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static float asin(float paramFloat)
  {
    return (float)Math.asin(paramFloat);
  }
  
  public static float atan(float paramFloat)
  {
    return (float)Math.atan(paramFloat);
  }
  
  public static float atan2(float paramFloat1, float paramFloat2)
  {
    return (float)Math.atan2(paramFloat1, paramFloat2);
  }
  
  public static float constrain(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 < paramFloat2) {
      paramFloat1 = paramFloat2;
    } else if (paramFloat1 > paramFloat3) {
      paramFloat1 = paramFloat3;
    }
    return paramFloat1;
  }
  
  public static int constrain(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 < paramInt2) {
      paramInt1 = paramInt2;
    } else if (paramInt1 > paramInt3) {
      paramInt1 = paramInt3;
    }
    return paramInt1;
  }
  
  public static long constrain(long paramLong1, long paramLong2, long paramLong3)
  {
    if (paramLong1 < paramLong2) {
      paramLong1 = paramLong2;
    } else if (paramLong1 > paramLong3) {
      paramLong1 = paramLong3;
    }
    return paramLong1;
  }
  
  public static float cross(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return paramFloat1 * paramFloat4 - paramFloat2 * paramFloat3;
  }
  
  public static float degrees(float paramFloat)
  {
    return 57.295784F * paramFloat;
  }
  
  public static float dist(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return (float)Math.hypot(paramFloat3 - paramFloat1, paramFloat4 - paramFloat2);
  }
  
  public static float dist(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    paramFloat1 = paramFloat4 - paramFloat1;
    paramFloat2 = paramFloat5 - paramFloat2;
    paramFloat3 = paramFloat6 - paramFloat3;
    return (float)Math.sqrt(paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 + paramFloat3 * paramFloat3);
  }
  
  public static float dot(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return paramFloat1 * paramFloat3 + paramFloat2 * paramFloat4;
  }
  
  public static float exp(float paramFloat)
  {
    return (float)Math.exp(paramFloat);
  }
  
  public static float lerp(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (paramFloat2 - paramFloat1) * paramFloat3 + paramFloat1;
  }
  
  public static float lerpDeg(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return ((paramFloat2 - paramFloat1 + 180.0F) % 360.0F - 180.0F) * paramFloat3 + paramFloat1;
  }
  
  public static float log(float paramFloat)
  {
    return (float)Math.log(paramFloat);
  }
  
  public static float mag(float paramFloat1, float paramFloat2)
  {
    return (float)Math.hypot(paramFloat1, paramFloat2);
  }
  
  public static float mag(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (float)Math.sqrt(paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 + paramFloat3 * paramFloat3);
  }
  
  public static float map(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5)
  {
    return (paramFloat4 - paramFloat3) * ((paramFloat5 - paramFloat1) / (paramFloat2 - paramFloat1)) + paramFloat3;
  }
  
  public static float max(float paramFloat1, float paramFloat2)
  {
    if (paramFloat1 <= paramFloat2) {
      paramFloat1 = paramFloat2;
    }
    return paramFloat1;
  }
  
  public static float max(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 > paramFloat2)
    {
      if (paramFloat1 > paramFloat3) {
        return paramFloat1;
      }
    }
    else if (paramFloat2 > paramFloat3)
    {
      paramFloat1 = paramFloat2;
      return paramFloat1;
    }
    paramFloat1 = paramFloat3;
    return paramFloat1;
  }
  
  public static float max(int paramInt1, int paramInt2)
  {
    float f;
    if (paramInt1 > paramInt2) {
      f = paramInt1;
    } else {
      f = paramInt2;
    }
    return f;
  }
  
  public static float max(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 > paramInt2) {
      if (paramInt1 > paramInt3) {}
    }
    float f;
    for (paramInt1 = paramInt3;; paramInt1 = paramInt2)
    {
      f = paramInt1;
      return f;
      if (paramInt2 <= paramInt3) {
        break;
      }
    }
    return f;
  }
  
  public static float min(float paramFloat1, float paramFloat2)
  {
    if (paramFloat1 >= paramFloat2) {
      paramFloat1 = paramFloat2;
    }
    return paramFloat1;
  }
  
  public static float min(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 < paramFloat2)
    {
      if (paramFloat1 < paramFloat3) {
        return paramFloat1;
      }
    }
    else if (paramFloat2 < paramFloat3)
    {
      paramFloat1 = paramFloat2;
      return paramFloat1;
    }
    paramFloat1 = paramFloat3;
    return paramFloat1;
  }
  
  public static float min(int paramInt1, int paramInt2)
  {
    float f;
    if (paramInt1 < paramInt2) {
      f = paramInt1;
    } else {
      f = paramInt2;
    }
    return f;
  }
  
  public static float min(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 < paramInt2) {
      if (paramInt1 < paramInt3) {}
    }
    float f;
    for (paramInt1 = paramInt3;; paramInt1 = paramInt2)
    {
      f = paramInt1;
      return f;
      if (paramInt2 >= paramInt3) {
        break;
      }
    }
    return f;
  }
  
  public static float norm(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (paramFloat3 - paramFloat1) / (paramFloat2 - paramFloat1);
  }
  
  public static float pow(float paramFloat1, float paramFloat2)
  {
    return (float)Math.pow(paramFloat1, paramFloat2);
  }
  
  public static float radians(float paramFloat)
  {
    return 0.017453292F * paramFloat;
  }
  
  public static float sq(float paramFloat)
  {
    return paramFloat * paramFloat;
  }
  
  public static float sqrt(float paramFloat)
  {
    return (float)Math.sqrt(paramFloat);
  }
  
  public static float tan(float paramFloat)
  {
    return (float)Math.tan(paramFloat);
  }
}
