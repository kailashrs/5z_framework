package android.opengl;

public class Matrix
{
  private static final float[] sTemp = new float[32];
  
  @Deprecated
  public Matrix() {}
  
  public static void frustumM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (paramFloat1 != paramFloat2)
    {
      if (paramFloat4 != paramFloat3)
      {
        if (paramFloat5 != paramFloat6)
        {
          if (paramFloat5 > 0.0F)
          {
            if (paramFloat6 > 0.0F)
            {
              float f1 = 1.0F / (paramFloat2 - paramFloat1);
              float f2 = 1.0F / (paramFloat4 - paramFloat3);
              float f3 = 1.0F / (paramFloat5 - paramFloat6);
              paramArrayOfFloat[(paramInt + 0)] = (paramFloat5 * f1 * 2.0F);
              paramArrayOfFloat[(paramInt + 5)] = (paramFloat5 * f2 * 2.0F);
              paramArrayOfFloat[(paramInt + 8)] = ((paramFloat2 + paramFloat1) * f1);
              paramArrayOfFloat[(paramInt + 9)] = ((paramFloat4 + paramFloat3) * f2);
              paramArrayOfFloat[(paramInt + 10)] = ((paramFloat6 + paramFloat5) * f3);
              paramArrayOfFloat[(paramInt + 14)] = (2.0F * (paramFloat6 * paramFloat5 * f3));
              paramArrayOfFloat[(paramInt + 11)] = -1.0F;
              paramArrayOfFloat[(paramInt + 1)] = 0.0F;
              paramArrayOfFloat[(paramInt + 2)] = 0.0F;
              paramArrayOfFloat[(paramInt + 3)] = 0.0F;
              paramArrayOfFloat[(paramInt + 4)] = 0.0F;
              paramArrayOfFloat[(paramInt + 6)] = 0.0F;
              paramArrayOfFloat[(paramInt + 7)] = 0.0F;
              paramArrayOfFloat[(paramInt + 12)] = 0.0F;
              paramArrayOfFloat[(paramInt + 13)] = 0.0F;
              paramArrayOfFloat[(paramInt + 15)] = 0.0F;
              return;
            }
            throw new IllegalArgumentException("far <= 0.0f");
          }
          throw new IllegalArgumentException("near <= 0.0f");
        }
        throw new IllegalArgumentException("near == far");
      }
      throw new IllegalArgumentException("top == bottom");
    }
    throw new IllegalArgumentException("left == right");
  }
  
  public static boolean invertM(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2)
  {
    float f1 = paramArrayOfFloat2[(paramInt2 + 0)];
    float f2 = paramArrayOfFloat2[(paramInt2 + 1)];
    float f3 = paramArrayOfFloat2[(paramInt2 + 2)];
    float f4 = paramArrayOfFloat2[(paramInt2 + 3)];
    float f5 = paramArrayOfFloat2[(paramInt2 + 4)];
    float f6 = paramArrayOfFloat2[(paramInt2 + 5)];
    float f7 = paramArrayOfFloat2[(paramInt2 + 6)];
    float f8 = paramArrayOfFloat2[(paramInt2 + 7)];
    float f9 = paramArrayOfFloat2[(paramInt2 + 8)];
    float f10 = paramArrayOfFloat2[(paramInt2 + 9)];
    float f11 = paramArrayOfFloat2[(paramInt2 + 10)];
    float f12 = paramArrayOfFloat2[(paramInt2 + 11)];
    float f13 = paramArrayOfFloat2[(paramInt2 + 12)];
    float f14 = paramArrayOfFloat2[(paramInt2 + 13)];
    float f15 = paramArrayOfFloat2[(paramInt2 + 14)];
    float f16 = paramArrayOfFloat2[(paramInt2 + 15)];
    float f17 = f11 * f16;
    float f18 = f15 * f12;
    float f19 = f7 * f16;
    float f20 = f15 * f8;
    float f21 = f7 * f12;
    float f22 = f11 * f8;
    float f23 = f3 * f16;
    float f24 = f15 * f4;
    float f25 = f3 * f12;
    float f26 = f11 * f4;
    float f27 = f3 * f8;
    float f28 = f7 * f4;
    float f29 = f17 * f6 + f20 * f10 + f21 * f14 - (f18 * f6 + f19 * f10 + f22 * f14);
    float f30 = f18 * f2 + f23 * f10 + f26 * f14 - (f17 * f2 + f24 * f10 + f25 * f14);
    float f31 = f19 * f2 + f24 * f6 + f27 * f14 - (f20 * f2 + f23 * f6 + f28 * f14);
    float f32 = f22 * f2 + f25 * f6 + f28 * f10 - (f21 * f2 + f26 * f6 + f27 * f10);
    float f33 = f9 * f14;
    float f34 = f13 * f10;
    float f35 = f5 * f14;
    float f36 = f13 * f6;
    float f37 = f5 * f10;
    float f38 = f9 * f6;
    float f39 = f1 * f14;
    f14 = f13 * f2;
    float f40 = f1 * f10;
    f10 = f9 * f2;
    f6 = f1 * f6;
    f2 = f5 * f2;
    float f41 = f1 * f29 + f5 * f30 + f9 * f31 + f13 * f32;
    if (f41 == 0.0F) {
      return false;
    }
    f41 = 1.0F / f41;
    paramArrayOfFloat1[paramInt1] = (f29 * f41);
    paramArrayOfFloat1[(1 + paramInt1)] = (f30 * f41);
    paramArrayOfFloat1[(2 + paramInt1)] = (f31 * f41);
    paramArrayOfFloat1[(3 + paramInt1)] = (f32 * f41);
    paramArrayOfFloat1[(4 + paramInt1)] = ((f18 * f5 + f19 * f9 + f22 * f13 - (f17 * f5 + f20 * f9 + f21 * f13)) * f41);
    paramArrayOfFloat1[(5 + paramInt1)] = ((f17 * f1 + f24 * f9 + f25 * f13 - (f18 * f1 + f23 * f9 + f26 * f13)) * f41);
    paramArrayOfFloat1[(6 + paramInt1)] = ((f20 * f1 + f23 * f5 + f28 * f13 - (f19 * f1 + f24 * f5 + f27 * f13)) * f41);
    paramArrayOfFloat1[(7 + paramInt1)] = ((f21 * f1 + f26 * f5 + f27 * f9 - (f22 * f1 + f25 * f5 + f28 * f9)) * f41);
    paramArrayOfFloat1[(8 + paramInt1)] = ((f33 * f8 + f36 * f12 + f37 * f16 - (f34 * f8 + f35 * f12 + f38 * f16)) * f41);
    paramArrayOfFloat1[(9 + paramInt1)] = ((f34 * f4 + f39 * f12 + f10 * f16 - (f33 * f4 + f14 * f12 + f40 * f16)) * f41);
    paramArrayOfFloat1[(10 + paramInt1)] = ((f35 * f4 + f14 * f8 + f6 * f16 - (f36 * f4 + f39 * f8 + f2 * f16)) * f41);
    paramArrayOfFloat1[(11 + paramInt1)] = ((f38 * f4 + f40 * f8 + f2 * f12 - (f37 * f4 + f10 * f8 + f6 * f12)) * f41);
    paramArrayOfFloat1[(12 + paramInt1)] = ((f35 * f11 + f38 * f15 + f34 * f7 - (f37 * f15 + f33 * f7 + f36 * f11)) * f41);
    paramArrayOfFloat1[(13 + paramInt1)] = ((f40 * f15 + f33 * f3 + f14 * f11 - (f39 * f11 + f10 * f15 + f34 * f3)) * f41);
    paramArrayOfFloat1[(14 + paramInt1)] = ((f39 * f7 + f2 * f15 + f36 * f3 - (f6 * f15 + f35 * f3 + f14 * f7)) * f41);
    paramArrayOfFloat1[(15 + paramInt1)] = ((f6 * f11 + f37 * f3 + f10 * f7 - (f40 * f7 + f2 * f11 + f38 * f3)) * f41);
    return true;
  }
  
  public static float length(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    return (float)Math.sqrt(paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 + paramFloat3 * paramFloat3);
  }
  
  public static native void multiplyMM(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, float[] paramArrayOfFloat3, int paramInt3);
  
  public static native void multiplyMV(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, float[] paramArrayOfFloat3, int paramInt3);
  
  public static void orthoM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    if (paramFloat1 != paramFloat2)
    {
      if (paramFloat3 != paramFloat4)
      {
        if (paramFloat5 != paramFloat6)
        {
          float f1 = 1.0F / (paramFloat2 - paramFloat1);
          float f2 = 1.0F / (paramFloat4 - paramFloat3);
          float f3 = 1.0F / (paramFloat6 - paramFloat5);
          paramFloat1 = -(paramFloat2 + paramFloat1);
          paramFloat2 = -(paramFloat4 + paramFloat3);
          paramFloat3 = -(paramFloat6 + paramFloat5);
          paramArrayOfFloat[(paramInt + 0)] = (2.0F * f1);
          paramArrayOfFloat[(paramInt + 5)] = (2.0F * f2);
          paramArrayOfFloat[(paramInt + 10)] = (-2.0F * f3);
          paramArrayOfFloat[(paramInt + 12)] = (paramFloat1 * f1);
          paramArrayOfFloat[(paramInt + 13)] = (paramFloat2 * f2);
          paramArrayOfFloat[(paramInt + 14)] = (paramFloat3 * f3);
          paramArrayOfFloat[(paramInt + 15)] = 1.0F;
          paramArrayOfFloat[(paramInt + 1)] = 0.0F;
          paramArrayOfFloat[(paramInt + 2)] = 0.0F;
          paramArrayOfFloat[(paramInt + 3)] = 0.0F;
          paramArrayOfFloat[(paramInt + 4)] = 0.0F;
          paramArrayOfFloat[(paramInt + 6)] = 0.0F;
          paramArrayOfFloat[(paramInt + 7)] = 0.0F;
          paramArrayOfFloat[(paramInt + 8)] = 0.0F;
          paramArrayOfFloat[(paramInt + 9)] = 0.0F;
          paramArrayOfFloat[(paramInt + 11)] = 0.0F;
          return;
        }
        throw new IllegalArgumentException("near == far");
      }
      throw new IllegalArgumentException("bottom == top");
    }
    throw new IllegalArgumentException("left == right");
  }
  
  public static void perspectiveM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f = 1.0F / (float)Math.tan(paramFloat1 * 0.008726646259971648D);
    paramFloat1 = 1.0F / (paramFloat3 - paramFloat4);
    paramArrayOfFloat[(paramInt + 0)] = (f / paramFloat2);
    paramArrayOfFloat[(paramInt + 1)] = 0.0F;
    paramArrayOfFloat[(paramInt + 2)] = 0.0F;
    paramArrayOfFloat[(paramInt + 3)] = 0.0F;
    paramArrayOfFloat[(paramInt + 4)] = 0.0F;
    paramArrayOfFloat[(paramInt + 5)] = f;
    paramArrayOfFloat[(paramInt + 6)] = 0.0F;
    paramArrayOfFloat[(paramInt + 7)] = 0.0F;
    paramArrayOfFloat[(paramInt + 8)] = 0.0F;
    paramArrayOfFloat[(paramInt + 9)] = 0.0F;
    paramArrayOfFloat[(paramInt + 10)] = ((paramFloat4 + paramFloat3) * paramFloat1);
    paramArrayOfFloat[(paramInt + 11)] = -1.0F;
    paramArrayOfFloat[(paramInt + 12)] = 0.0F;
    paramArrayOfFloat[(paramInt + 13)] = 0.0F;
    paramArrayOfFloat[(paramInt + 14)] = (2.0F * paramFloat4 * paramFloat3 * paramFloat1);
    paramArrayOfFloat[(paramInt + 15)] = 0.0F;
  }
  
  public static void rotateM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    synchronized (sTemp)
    {
      setRotateM(sTemp, 0, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      multiplyMM(sTemp, 16, paramArrayOfFloat, paramInt, sTemp, 0);
      System.arraycopy(sTemp, 16, paramArrayOfFloat, paramInt, 16);
      return;
    }
  }
  
  public static void rotateM(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    synchronized (sTemp)
    {
      setRotateM(sTemp, 0, paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      multiplyMM(paramArrayOfFloat1, paramInt1, paramArrayOfFloat2, paramInt2, sTemp, 0);
      return;
    }
  }
  
  public static void scaleM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    for (int i = 0; i < 4; i++)
    {
      int j = paramInt + i;
      paramArrayOfFloat[j] *= paramFloat1;
      int k = 4 + j;
      paramArrayOfFloat[k] *= paramFloat2;
      k = 8 + j;
      paramArrayOfFloat[k] *= paramFloat3;
    }
  }
  
  public static void scaleM(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    for (int i = 0; i < 4; i++)
    {
      int j = paramInt1 + i;
      int k = paramInt2 + i;
      paramArrayOfFloat2[k] *= paramFloat1;
      paramArrayOfFloat2[(4 + k)] *= paramFloat2;
      paramArrayOfFloat2[(8 + k)] *= paramFloat3;
      paramArrayOfFloat1[(12 + j)] = paramArrayOfFloat2[(12 + k)];
    }
  }
  
  public static void setIdentityM(float[] paramArrayOfFloat, int paramInt)
  {
    int i = 0;
    for (int j = 0; j < 16; j++) {
      paramArrayOfFloat[(paramInt + j)] = 0.0F;
    }
    for (j = i; j < 16; j += 5) {
      paramArrayOfFloat[(paramInt + j)] = 1.0F;
    }
  }
  
  public static void setLookAtM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9)
  {
    paramFloat4 -= paramFloat1;
    paramFloat5 -= paramFloat2;
    float f1 = paramFloat6 - paramFloat3;
    paramFloat6 = 1.0F / length(paramFloat4, paramFloat5, f1);
    paramFloat4 *= paramFloat6;
    paramFloat5 *= paramFloat6;
    paramFloat6 = f1 * paramFloat6;
    f1 = paramFloat5 * paramFloat9 - paramFloat6 * paramFloat8;
    paramFloat9 = paramFloat6 * paramFloat7 - paramFloat4 * paramFloat9;
    float f2 = paramFloat4 * paramFloat8 - paramFloat5 * paramFloat7;
    paramFloat7 = 1.0F / length(f1, paramFloat9, f2);
    paramFloat8 = f1 * paramFloat7;
    paramFloat9 *= paramFloat7;
    paramFloat7 = f2 * paramFloat7;
    paramArrayOfFloat[(paramInt + 0)] = paramFloat8;
    paramArrayOfFloat[(paramInt + 1)] = (paramFloat9 * paramFloat6 - paramFloat7 * paramFloat5);
    paramArrayOfFloat[(paramInt + 2)] = (-paramFloat4);
    paramArrayOfFloat[(paramInt + 3)] = 0.0F;
    paramArrayOfFloat[(paramInt + 4)] = paramFloat9;
    paramArrayOfFloat[(paramInt + 5)] = (paramFloat7 * paramFloat4 - paramFloat8 * paramFloat6);
    paramArrayOfFloat[(paramInt + 6)] = (-paramFloat5);
    paramArrayOfFloat[(paramInt + 7)] = 0.0F;
    paramArrayOfFloat[(paramInt + 8)] = paramFloat7;
    paramArrayOfFloat[(paramInt + 9)] = (paramFloat8 * paramFloat5 - paramFloat9 * paramFloat4);
    paramArrayOfFloat[(paramInt + 10)] = (-paramFloat6);
    paramArrayOfFloat[(paramInt + 11)] = 0.0F;
    paramArrayOfFloat[(paramInt + 12)] = 0.0F;
    paramArrayOfFloat[(paramInt + 13)] = 0.0F;
    paramArrayOfFloat[(paramInt + 14)] = 0.0F;
    paramArrayOfFloat[(paramInt + 15)] = 1.0F;
    translateM(paramArrayOfFloat, paramInt, -paramFloat1, -paramFloat2, -paramFloat3);
  }
  
  public static void setRotateEulerM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1 = paramFloat1 * 0.017453292F;
    float f2 = paramFloat2 * 0.017453292F;
    float f3 = 0.017453292F * paramFloat3;
    paramFloat1 = (float)Math.cos(f1);
    paramFloat3 = (float)Math.sin(f1);
    paramFloat2 = (float)Math.cos(f2);
    f1 = (float)Math.sin(f2);
    f2 = (float)Math.cos(f3);
    f3 = (float)Math.sin(f3);
    float f4 = paramFloat1 * f1;
    float f5 = paramFloat3 * f1;
    paramArrayOfFloat[(paramInt + 0)] = (paramFloat2 * f2);
    paramArrayOfFloat[(paramInt + 1)] = (-paramFloat2 * f3);
    paramArrayOfFloat[(paramInt + 2)] = f1;
    paramArrayOfFloat[(paramInt + 3)] = 0.0F;
    paramArrayOfFloat[(paramInt + 4)] = (f4 * f2 + paramFloat1 * f3);
    paramArrayOfFloat[(paramInt + 5)] = (-f4 * f3 + paramFloat1 * f2);
    paramArrayOfFloat[(paramInt + 6)] = (-paramFloat3 * paramFloat2);
    paramArrayOfFloat[(paramInt + 7)] = 0.0F;
    paramArrayOfFloat[(paramInt + 8)] = (-f5 * f2 + paramFloat3 * f3);
    paramArrayOfFloat[(paramInt + 9)] = (f5 * f3 + paramFloat3 * f2);
    paramArrayOfFloat[(paramInt + 10)] = (paramFloat1 * paramFloat2);
    paramArrayOfFloat[(paramInt + 11)] = 0.0F;
    paramArrayOfFloat[(paramInt + 12)] = 0.0F;
    paramArrayOfFloat[(paramInt + 13)] = 0.0F;
    paramArrayOfFloat[(paramInt + 14)] = 0.0F;
    paramArrayOfFloat[(paramInt + 15)] = 1.0F;
  }
  
  public static void setRotateM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    paramArrayOfFloat[(paramInt + 3)] = 0.0F;
    paramArrayOfFloat[(paramInt + 7)] = 0.0F;
    paramArrayOfFloat[(paramInt + 11)] = 0.0F;
    paramArrayOfFloat[(paramInt + 12)] = 0.0F;
    paramArrayOfFloat[(paramInt + 13)] = 0.0F;
    paramArrayOfFloat[(paramInt + 14)] = 0.0F;
    paramArrayOfFloat[(paramInt + 15)] = 1.0F;
    paramFloat1 = 0.017453292F * paramFloat1;
    float f1 = (float)Math.sin(paramFloat1);
    float f2 = (float)Math.cos(paramFloat1);
    if ((1.0F == paramFloat2) && (0.0F == paramFloat3) && (0.0F == paramFloat4))
    {
      paramArrayOfFloat[(paramInt + 5)] = f2;
      paramArrayOfFloat[(paramInt + 10)] = f2;
      paramArrayOfFloat[(paramInt + 6)] = f1;
      paramArrayOfFloat[(paramInt + 9)] = (-f1);
      paramArrayOfFloat[(paramInt + 1)] = 0.0F;
      paramArrayOfFloat[(paramInt + 2)] = 0.0F;
      paramArrayOfFloat[(paramInt + 4)] = 0.0F;
      paramArrayOfFloat[(paramInt + 8)] = 0.0F;
      paramArrayOfFloat[(paramInt + 0)] = 1.0F;
    }
    else if ((0.0F == paramFloat2) && (1.0F == paramFloat3) && (0.0F == paramFloat4))
    {
      paramArrayOfFloat[(paramInt + 0)] = f2;
      paramArrayOfFloat[(paramInt + 10)] = f2;
      paramArrayOfFloat[(paramInt + 8)] = f1;
      paramArrayOfFloat[(paramInt + 2)] = (-f1);
      paramArrayOfFloat[(paramInt + 1)] = 0.0F;
      paramArrayOfFloat[(paramInt + 4)] = 0.0F;
      paramArrayOfFloat[(paramInt + 6)] = 0.0F;
      paramArrayOfFloat[(paramInt + 9)] = 0.0F;
      paramArrayOfFloat[(paramInt + 5)] = 1.0F;
    }
    else
    {
      if ((0.0F != paramFloat2) || (0.0F != paramFloat3) || (1.0F != paramFloat4)) {
        break label327;
      }
      paramArrayOfFloat[(paramInt + 0)] = f2;
      paramArrayOfFloat[(paramInt + 5)] = f2;
      paramArrayOfFloat[(paramInt + 1)] = f1;
      paramArrayOfFloat[(paramInt + 4)] = (-f1);
      paramArrayOfFloat[(paramInt + 2)] = 0.0F;
      paramArrayOfFloat[(paramInt + 6)] = 0.0F;
      paramArrayOfFloat[(paramInt + 8)] = 0.0F;
      paramArrayOfFloat[(paramInt + 9)] = 0.0F;
      paramArrayOfFloat[(paramInt + 10)] = 1.0F;
    }
    return;
    label327:
    paramFloat1 = length(paramFloat2, paramFloat3, paramFloat4);
    if (1.0F != paramFloat1)
    {
      f3 = 1.0F / paramFloat1;
      paramFloat1 = paramFloat2 * f3;
      paramFloat2 = paramFloat3 * f3;
      paramFloat4 *= f3;
      paramFloat3 = paramFloat1;
      paramFloat1 = paramFloat4;
    }
    else
    {
      f3 = paramFloat2;
      paramFloat2 = paramFloat3;
      paramFloat1 = paramFloat4;
      paramFloat3 = f3;
    }
    float f4 = 1.0F - f2;
    float f3 = paramFloat3 * paramFloat2;
    paramFloat4 = paramFloat2 * paramFloat1;
    float f5 = paramFloat1 * paramFloat3;
    float f6 = paramFloat3 * f1;
    float f7 = paramFloat2 * f1;
    f1 = paramFloat1 * f1;
    paramArrayOfFloat[(paramInt + 0)] = (paramFloat3 * paramFloat3 * f4 + f2);
    paramArrayOfFloat[(paramInt + 4)] = (f3 * f4 - f1);
    paramArrayOfFloat[(paramInt + 8)] = (f5 * f4 + f7);
    paramArrayOfFloat[(paramInt + 1)] = (f3 * f4 + f1);
    paramArrayOfFloat[(paramInt + 5)] = (paramFloat2 * paramFloat2 * f4 + f2);
    paramArrayOfFloat[(paramInt + 9)] = (paramFloat4 * f4 - f6);
    paramArrayOfFloat[(paramInt + 2)] = (f5 * f4 - f7);
    paramArrayOfFloat[(paramInt + 6)] = (paramFloat4 * f4 + f6);
    paramArrayOfFloat[(paramInt + 10)] = (paramFloat1 * paramFloat1 * f4 + f2);
  }
  
  public static void translateM(float[] paramArrayOfFloat, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    for (int i = 0; i < 4; i++)
    {
      int j = paramInt + i;
      int k = 12 + j;
      paramArrayOfFloat[k] += paramArrayOfFloat[j] * paramFloat1 + paramArrayOfFloat[(4 + j)] * paramFloat2 + paramArrayOfFloat[(8 + j)] * paramFloat3;
    }
  }
  
  public static void translateM(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    int i = 0;
    for (int j = 0; j < 12; j++) {
      paramArrayOfFloat1[(paramInt1 + j)] = paramArrayOfFloat2[(paramInt2 + j)];
    }
    for (j = i; j < 4; j++)
    {
      i = paramInt2 + j;
      paramArrayOfFloat1[(12 + (paramInt1 + j))] = (paramArrayOfFloat2[i] * paramFloat1 + paramArrayOfFloat2[(4 + i)] * paramFloat2 + paramArrayOfFloat2[(8 + i)] * paramFloat3 + paramArrayOfFloat2[(12 + i)]);
    }
  }
  
  public static void transposeM(float[] paramArrayOfFloat1, int paramInt1, float[] paramArrayOfFloat2, int paramInt2)
  {
    for (int i = 0; i < 4; i++)
    {
      int j = i * 4 + paramInt2;
      paramArrayOfFloat1[(i + paramInt1)] = paramArrayOfFloat2[j];
      paramArrayOfFloat1[(i + 4 + paramInt1)] = paramArrayOfFloat2[(j + 1)];
      paramArrayOfFloat1[(i + 8 + paramInt1)] = paramArrayOfFloat2[(j + 2)];
      paramArrayOfFloat1[(i + 12 + paramInt1)] = paramArrayOfFloat2[(j + 3)];
    }
  }
}
