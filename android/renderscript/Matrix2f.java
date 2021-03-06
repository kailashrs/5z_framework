package android.renderscript;

public class Matrix2f
{
  final float[] mMat = new float[4];
  
  public Matrix2f()
  {
    loadIdentity();
  }
  
  public Matrix2f(float[] paramArrayOfFloat)
  {
    System.arraycopy(paramArrayOfFloat, 0, mMat, 0, mMat.length);
  }
  
  public float get(int paramInt1, int paramInt2)
  {
    return mMat[(paramInt1 * 2 + paramInt2)];
  }
  
  public float[] getArray()
  {
    return mMat;
  }
  
  public void load(Matrix2f paramMatrix2f)
  {
    System.arraycopy(paramMatrix2f.getArray(), 0, mMat, 0, mMat.length);
  }
  
  public void loadIdentity()
  {
    mMat[0] = 1.0F;
    mMat[1] = 0.0F;
    mMat[2] = 0.0F;
    mMat[3] = 1.0F;
  }
  
  public void loadMultiply(Matrix2f paramMatrix2f1, Matrix2f paramMatrix2f2)
  {
    for (int i = 0; i < 2; i++)
    {
      float f1 = 0.0F;
      float f2 = 0.0F;
      for (int j = 0; j < 2; j++)
      {
        float f3 = paramMatrix2f2.get(i, j);
        f2 += paramMatrix2f1.get(j, 0) * f3;
        f1 += paramMatrix2f1.get(j, 1) * f3;
      }
      set(i, 0, f2);
      set(i, 1, f1);
    }
  }
  
  public void loadRotate(float paramFloat)
  {
    float f = paramFloat * 0.017453292F;
    paramFloat = (float)Math.cos(f);
    f = (float)Math.sin(f);
    mMat[0] = paramFloat;
    mMat[1] = (-f);
    mMat[2] = f;
    mMat[3] = paramFloat;
  }
  
  public void loadScale(float paramFloat1, float paramFloat2)
  {
    loadIdentity();
    mMat[0] = paramFloat1;
    mMat[3] = paramFloat2;
  }
  
  public void multiply(Matrix2f paramMatrix2f)
  {
    Matrix2f localMatrix2f = new Matrix2f();
    localMatrix2f.loadMultiply(this, paramMatrix2f);
    load(localMatrix2f);
  }
  
  public void rotate(float paramFloat)
  {
    Matrix2f localMatrix2f = new Matrix2f();
    localMatrix2f.loadRotate(paramFloat);
    multiply(localMatrix2f);
  }
  
  public void scale(float paramFloat1, float paramFloat2)
  {
    Matrix2f localMatrix2f = new Matrix2f();
    localMatrix2f.loadScale(paramFloat1, paramFloat2);
    multiply(localMatrix2f);
  }
  
  public void set(int paramInt1, int paramInt2, float paramFloat)
  {
    mMat[(paramInt1 * 2 + paramInt2)] = paramFloat;
  }
  
  public void transpose()
  {
    float f = mMat[1];
    mMat[1] = mMat[2];
    mMat[2] = f;
  }
}
