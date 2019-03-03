package android.renderscript;

public class Matrix3f
{
  final float[] mMat = new float[9];
  
  public Matrix3f()
  {
    loadIdentity();
  }
  
  public Matrix3f(float[] paramArrayOfFloat)
  {
    System.arraycopy(paramArrayOfFloat, 0, mMat, 0, mMat.length);
  }
  
  public float get(int paramInt1, int paramInt2)
  {
    return mMat[(paramInt1 * 3 + paramInt2)];
  }
  
  public float[] getArray()
  {
    return mMat;
  }
  
  public void load(Matrix3f paramMatrix3f)
  {
    System.arraycopy(paramMatrix3f.getArray(), 0, mMat, 0, mMat.length);
  }
  
  public void loadIdentity()
  {
    mMat[0] = 1.0F;
    mMat[1] = 0.0F;
    mMat[2] = 0.0F;
    mMat[3] = 0.0F;
    mMat[4] = 1.0F;
    mMat[5] = 0.0F;
    mMat[6] = 0.0F;
    mMat[7] = 0.0F;
    mMat[8] = 1.0F;
  }
  
  public void loadMultiply(Matrix3f paramMatrix3f1, Matrix3f paramMatrix3f2)
  {
    for (int i = 0; i < 3; i++)
    {
      float f1 = 0.0F;
      float f2 = 0.0F;
      float f3 = 0.0F;
      for (int j = 0; j < 3; j++)
      {
        float f4 = paramMatrix3f2.get(i, j);
        f3 += paramMatrix3f1.get(j, 0) * f4;
        f2 += paramMatrix3f1.get(j, 1) * f4;
        f1 += paramMatrix3f1.get(j, 2) * f4;
      }
      set(i, 0, f3);
      set(i, 1, f2);
      set(i, 2, f1);
    }
  }
  
  public void loadRotate(float paramFloat)
  {
    loadIdentity();
    float f = paramFloat * 0.017453292F;
    paramFloat = (float)Math.cos(f);
    f = (float)Math.sin(f);
    mMat[0] = paramFloat;
    mMat[1] = (-f);
    mMat[3] = f;
    mMat[4] = paramFloat;
  }
  
  public void loadRotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
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
    paramFloat4 = paramFloat2 * paramFloat1;
    float f6 = paramFloat1 * f2;
    float f7 = paramFloat3 * f2;
    f2 = paramFloat2 * f2;
    mMat[0] = (paramFloat1 * paramFloat1 * f4 + f1);
    mMat[3] = (f5 * f4 - f2);
    mMat[6] = (paramFloat4 * f4 + f7);
    mMat[1] = (f5 * f4 + f2);
    mMat[4] = (paramFloat3 * paramFloat3 * f4 + f1);
    mMat[7] = (f3 * f4 - f6);
    mMat[2] = (paramFloat4 * f4 - f7);
    mMat[5] = (f3 * f4 + f6);
    mMat[8] = (paramFloat2 * paramFloat2 * f4 + f1);
  }
  
  public void loadScale(float paramFloat1, float paramFloat2)
  {
    loadIdentity();
    mMat[0] = paramFloat1;
    mMat[4] = paramFloat2;
  }
  
  public void loadScale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    loadIdentity();
    mMat[0] = paramFloat1;
    mMat[4] = paramFloat2;
    mMat[8] = paramFloat3;
  }
  
  public void loadTranslate(float paramFloat1, float paramFloat2)
  {
    loadIdentity();
    mMat[6] = paramFloat1;
    mMat[7] = paramFloat2;
  }
  
  public void multiply(Matrix3f paramMatrix3f)
  {
    Matrix3f localMatrix3f = new Matrix3f();
    localMatrix3f.loadMultiply(this, paramMatrix3f);
    load(localMatrix3f);
  }
  
  public void rotate(float paramFloat)
  {
    Matrix3f localMatrix3f = new Matrix3f();
    localMatrix3f.loadRotate(paramFloat);
    multiply(localMatrix3f);
  }
  
  public void rotate(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    Matrix3f localMatrix3f = new Matrix3f();
    localMatrix3f.loadRotate(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    multiply(localMatrix3f);
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    Matrix3f localMatrix3f = new Matrix3f();
    localMatrix3f.loadScale(paramFloat1, paramFloat2);
    multiply(localMatrix3f);
  }
  
  public void scale(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    Matrix3f localMatrix3f = new Matrix3f();
    localMatrix3f.loadScale(paramFloat1, paramFloat2, paramFloat3);
    multiply(localMatrix3f);
  }
  
  public void set(int paramInt1, int paramInt2, float paramFloat)
  {
    mMat[(paramInt1 * 3 + paramInt2)] = paramFloat;
  }
  
  public void translate(float paramFloat1, float paramFloat2)
  {
    Matrix3f localMatrix3f = new Matrix3f();
    localMatrix3f.loadTranslate(paramFloat1, paramFloat2);
    multiply(localMatrix3f);
  }
  
  public void transpose()
  {
    for (int i = 0; i < 2; i++) {
      for (int j = i + 1; j < 3; j++)
      {
        float f = mMat[(i * 3 + j)];
        mMat[(i * 3 + j)] = mMat[(j * 3 + i)];
        mMat[(j * 3 + i)] = f;
      }
    }
  }
}
