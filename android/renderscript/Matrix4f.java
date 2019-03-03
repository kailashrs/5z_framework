package android.renderscript;

public class Matrix4f
{
  final float[] mMat = new float[16];
  
  public Matrix4f()
  {
    loadIdentity();
  }
  
  public Matrix4f(float[] paramArrayOfFloat)
  {
    System.arraycopy(paramArrayOfFloat, 0, mMat, 0, mMat.length);
  }
  
  private float computeCofactor(int paramInt1, int paramInt2)
  {
    int i = (paramInt1 + 1) % 4;
    int j = (paramInt1 + 2) % 4;
    int k = (paramInt1 + 3) % 4;
    int m = (paramInt2 + 1) % 4;
    int n = (paramInt2 + 2) % 4;
    int i1 = (paramInt2 + 3) % 4;
    float f = mMat[(4 * m + i)] * (mMat[(4 * n + j)] * mMat[(4 * i1 + k)] - mMat[(4 * i1 + j)] * mMat[(4 * n + k)]) - mMat[(4 * n + i)] * (mMat[(4 * m + j)] * mMat[(4 * i1 + k)] - mMat[(4 * i1 + j)] * mMat[(4 * m + k)]) + mMat[(4 * i1 + i)] * (mMat[(4 * m + j)] * mMat[(4 * n + k)] - mMat[(4 * n + j)] * mMat[(4 * m + k)]);
    if ((paramInt1 + paramInt2 & 0x1) != 0) {
      f = -f;
    }
    return f;
  }
  
  public float get(int paramInt1, int paramInt2)
  {
    return mMat[(paramInt1 * 4 + paramInt2)];
  }
  
  public float[] getArray()
  {
    return mMat;
  }
  
  public boolean inverse()
  {
    Matrix4f localMatrix4f = new Matrix4f();
    int i = 0;
    for (int j = 0; j < 4; j++) {
      for (int k = 0; k < 4; k++) {
        mMat[(4 * j + k)] = computeCofactor(j, k);
      }
    }
    float f = mMat[0] * mMat[0] + mMat[4] * mMat[1] + mMat[8] * mMat[2] + mMat[12] * mMat[3];
    if (Math.abs(f) < 1.0E-6D) {
      return false;
    }
    f = 1.0F / f;
    for (j = i; j < 16; j++) {
      mMat[j] *= f;
    }
    return true;
  }
  
  public boolean inverseTranspose()
  {
    Matrix4f localMatrix4f = new Matrix4f();
    int i = 0;
    for (int j = 0; j < 4; j++) {
      for (int k = 0; k < 4; k++) {
        mMat[(4 * k + j)] = computeCofactor(j, k);
      }
    }
    float f = mMat[0] * mMat[0] + mMat[4] * mMat[4] + mMat[8] * mMat[8] + mMat[12] * mMat[12];
    if (Math.abs(f) < 1.0E-6D) {
      return false;
    }
    f = 1.0F / f;
    for (j = i; j < 16; j++) {
      mMat[j] *= f;
    }
    return true;
  }
  
  public void load(Matrix3f paramMatrix3f)
  {
    mMat[0] = mMat[0];
    mMat[1] = mMat[1];
    mMat[2] = mMat[2];
    mMat[3] = 0.0F;
    mMat[4] = mMat[3];
    mMat[5] = mMat[4];
    mMat[6] = mMat[5];
    mMat[7] = 0.0F;
    mMat[8] = mMat[6];
    mMat[9] = mMat[7];
    mMat[10] = mMat[8];
    mMat[11] = 0.0F;
    mMat[12] = 0.0F;
    mMat[13] = 0.0F;
    mMat[14] = 0.0F;
    mMat[15] = 1.0F;
  }
  
  public void load(Matrix4f paramMatrix4f)
  {
    System.arraycopy(paramMatrix4f.getArray(), 0, mMat, 0, mMat.length);
  }
  
  public void loadFrustum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    loadIdentity();
    mMat[0] = (2.0F * paramFloat5 / (paramFloat2 - paramFloat1));
    mMat[5] = (2.0F * paramFloat5 / (paramFloat4 - paramFloat3));
    mMat[8] = ((paramFloat2 + paramFloat1) / (paramFloat2 - paramFloat1));
    mMat[9] = ((paramFloat4 + paramFloat3) / (paramFloat4 - paramFloat3));
    mMat[10] = (-(paramFloat6 + paramFloat5) / (paramFloat6 - paramFloat5));
    mMat[11] = -1.0F;
    mMat[14] = (-2.0F * paramFloat6 * paramFloat5 / (paramFloat6 - paramFloat5));
    mMat[15] = 0.0F;
  }
  
  public void loadIdentity()
  {
    mMat[0] = 1.0F;
    mMat[1] = 0.0F;
    mMat[2] = 0.0F;
    mMat[3] = 0.0F;
    mMat[4] = 0.0F;
    mMat[5] = 1.0F;
    mMat[6] = 0.0F;
    mMat[7] = 0.0F;
    mMat[8] = 0.0F;
    mMat[9] = 0.0F;
    mMat[10] = 1.0F;
    mMat[11] = 0.0F;
    mMat[12] = 0.0F;
    mMat[13] = 0.0F;
    mMat[14] = 0.0F;
    mMat[15] = 1.0F;
  }
  
  public void loadMultiply(Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2)
  {
    for (int i = 0; i < 4; i++)
    {
      float f1 = 0.0F;
      float f2 = 0.0F;
      float f3 = 0.0F;
      float f4 = 0.0F;
      for (int j = 0; j < 4; j++)
      {
        float f5 = paramMatrix4f2.get(i, j);
        f4 += paramMatrix4f1.get(j, 0) * f5;
        f3 += paramMatrix4f1.get(j, 1) * f5;
        f2 += paramMatrix4f1.get(j, 2) * f5;
        f1 += paramMatrix4f1.get(j, 3) * f5;
      }
      set(i, 0, f4);
      set(i, 1, f3);
      set(i, 2, f2);
      set(i, 3, f1);
    }
  }
  
  public void loadOrtho(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    loadIdentity();
    mMat[0] = (2.0F / (paramFloat2 - paramFloat1));
    mMat[5] = (2.0F / (paramFloat4 - paramFloat3));
    mMat[10] = (-2.0F / (paramFloat6 - paramFloat5));
    mMat[12] = (-(paramFloat2 + paramFloat1) / (paramFloat2 - paramFloat1));
    mMat[13] = (-(paramFloat4 + paramFloat3) / (paramFloat4 - paramFloat3));
    mMat[14] = (-(paramFloat6 + paramFloat5) / (paramFloat6 - paramFloat5));
  }
  
  public void loadOrthoWindow(int paramInt1, int paramInt2)
  {
    loadOrtho(0.0F, paramInt1, paramInt2, 0.0F, -1.0F, 1.0F);
  }
  
  public void loadPerspective(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float f = (float)Math.tan((float)(paramFloat1 * 3.141592653589793D / 360.0D)) * paramFloat3;
    paramFloat1 = -f;
    loadFrustum(paramFloat1 * paramFloat2, f * paramFloat2, paramFloat1, f, paramFloat3, paramFloat4);
  }
  
  public void loadProjectionNormalized(int paramInt1, int paramInt2)
  {
    Matrix4f localMatrix4f1 = new Matrix4f();
    Matrix4f localMatrix4f2 = new Matrix4f();
    float f;
    if (paramInt1 > paramInt2)
    {
      f = paramInt1 / paramInt2;
      localMatrix4f1.loadFrustum(-f, f, -1.0F, 1.0F, 1.0F, 100.0F);
    }
    else
    {
      f = paramInt2 / paramInt1;
      localMatrix4f1.loadFrustum(-1.0F, 1.0F, -f, f, 1.0F, 100.0F);
    }
    localMatrix4f2.loadRotate(180.0F, 0.0F, 1.0F, 0.0F);
    localMatrix4f1.loadMultiply(localMatrix4f1, localMatrix4f2);
    localMatrix4f2.loadScale(-2.0F, 2.0F, 1.0F);
    localMatrix4f1.loadMultiply(localMatrix4f1, localMatrix4f2);
    localMatrix4f2.loadTranslate(0.0F, 0.0F, 2.0F);
    localMatrix4f1.loadMultiply(localMatrix4f1, localMatrix4f2);
    load(localMatrix4f1);
  }
  
  public void loadRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    mMat[3] = 0.0F;
    mMat[7] = 0.0F;
    mMat[11] = 0.0F;
    mMat[12] = 0.0F;
    mMat[13] = 0.0F;
    mMat[14] = 0.0F;
    mMat[15] = 1.0F;
    paramFloat1 = 0.017453292F * paramFloat1;
    float f1 = (float)Math.cos(paramFloat1);
    float f2 = (float)Math.sin(paramFloat1);
    paramFloat1 = (float)Math.sqrt(paramFloat2 * paramFloat2 + paramFloat3 * paramFloat3 + paramFloat4 * paramFloat4);
    if (paramFloat1 == 1.0F)
    {
      f3 = 1.0F / paramFloat1;
      paramFloat1 = paramFloat2 * f3;
      paramFloat3 *= f3;
      paramFloat2 = paramFloat4 * f3;
    }
    else
    {
      paramFloat1 = paramFloat2;
      paramFloat2 = paramFloat4;
    }
    float f4 = 1.0F - f1;
    float f5 = paramFloat1 * paramFloat3;
    float f3 = paramFloat3 * paramFloat2;
    float f6 = paramFloat2 * paramFloat1;
    paramFloat4 = paramFloat1 * f2;
    float f7 = paramFloat3 * f2;
    f2 = paramFloat2 * f2;
    mMat[0] = (paramFloat1 * paramFloat1 * f4 + f1);
    mMat[4] = (f5 * f4 - f2);
    mMat[8] = (f6 * f4 + f7);
    mMat[1] = (f5 * f4 + f2);
    mMat[5] = (paramFloat3 * paramFloat3 * f4 + f1);
    mMat[9] = (f3 * f4 - paramFloat4);
    mMat[2] = (f6 * f4 - f7);
    mMat[6] = (f3 * f4 + paramFloat4);
    mMat[10] = (paramFloat2 * paramFloat2 * f4 + f1);
  }
  
  public void loadScale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    loadIdentity();
    mMat[0] = paramFloat1;
    mMat[5] = paramFloat2;
    mMat[10] = paramFloat3;
  }
  
  public void loadTranslate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    loadIdentity();
    mMat[12] = paramFloat1;
    mMat[13] = paramFloat2;
    mMat[14] = paramFloat3;
  }
  
  public void multiply(Matrix4f paramMatrix4f)
  {
    Matrix4f localMatrix4f = new Matrix4f();
    localMatrix4f.loadMultiply(this, paramMatrix4f);
    load(localMatrix4f);
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    Matrix4f localMatrix4f = new Matrix4f();
    localMatrix4f.loadRotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    multiply(localMatrix4f);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    Matrix4f localMatrix4f = new Matrix4f();
    localMatrix4f.loadScale(paramFloat1, paramFloat2, paramFloat3);
    multiply(localMatrix4f);
  }
  
  public void set(int paramInt1, int paramInt2, float paramFloat)
  {
    mMat[(paramInt1 * 4 + paramInt2)] = paramFloat;
  }
  
  public void translate(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    Matrix4f localMatrix4f = new Matrix4f();
    localMatrix4f.loadTranslate(paramFloat1, paramFloat2, paramFloat3);
    multiply(localMatrix4f);
  }
  
  public void transpose()
  {
    for (int i = 0; i < 3; i++) {
      for (int j = i + 1; j < 4; j++)
      {
        float f = mMat[(i * 4 + j)];
        mMat[(i * 4 + j)] = mMat[(j * 4 + i)];
        mMat[(j * 4 + i)] = f;
      }
    }
  }
}
