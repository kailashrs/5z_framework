package android.graphics;

import java.util.Arrays;

public class ColorMatrix
{
  private final float[] mArray = new float[20];
  
  public ColorMatrix()
  {
    reset();
  }
  
  public ColorMatrix(ColorMatrix paramColorMatrix)
  {
    System.arraycopy(mArray, 0, mArray, 0, 20);
  }
  
  public ColorMatrix(float[] paramArrayOfFloat)
  {
    System.arraycopy(paramArrayOfFloat, 0, mArray, 0, 20);
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ColorMatrix)) {
      return false;
    }
    paramObject = mArray;
    for (int i = 0; i < 20; i++) {
      if (paramObject[i] != mArray[i]) {
        return false;
      }
    }
    return true;
  }
  
  public final float[] getArray()
  {
    return mArray;
  }
  
  public void postConcat(ColorMatrix paramColorMatrix)
  {
    setConcat(paramColorMatrix, this);
  }
  
  public void preConcat(ColorMatrix paramColorMatrix)
  {
    setConcat(this, paramColorMatrix);
  }
  
  public void reset()
  {
    float[] arrayOfFloat = mArray;
    Arrays.fill(arrayOfFloat, 0.0F);
    arrayOfFloat[18] = 1.0F;
    arrayOfFloat[12] = 1.0F;
    arrayOfFloat[6] = 1.0F;
    arrayOfFloat[0] = 1.0F;
  }
  
  public void set(ColorMatrix paramColorMatrix)
  {
    System.arraycopy(mArray, 0, mArray, 0, 20);
  }
  
  public void set(float[] paramArrayOfFloat)
  {
    System.arraycopy(paramArrayOfFloat, 0, mArray, 0, 20);
  }
  
  public void setConcat(ColorMatrix paramColorMatrix1, ColorMatrix paramColorMatrix2)
  {
    float[] arrayOfFloat;
    if ((paramColorMatrix1 != this) && (paramColorMatrix2 != this)) {
      arrayOfFloat = mArray;
    } else {
      arrayOfFloat = new float[20];
    }
    paramColorMatrix1 = mArray;
    paramColorMatrix2 = mArray;
    int i = 0;
    int j = 0;
    while (j < 20)
    {
      int k = 0;
      while (k < 4)
      {
        arrayOfFloat[i] = (paramColorMatrix1[(j + 0)] * paramColorMatrix2[(k + 0)] + paramColorMatrix1[(j + 1)] * paramColorMatrix2[(k + 5)] + paramColorMatrix1[(j + 2)] * paramColorMatrix2[(k + 10)] + paramColorMatrix1[(j + 3)] * paramColorMatrix2[(k + 15)]);
        k++;
        i++;
      }
      k = i + 1;
      arrayOfFloat[i] = (paramColorMatrix1[(j + 0)] * paramColorMatrix2[4] + paramColorMatrix1[(j + 1)] * paramColorMatrix2[9] + paramColorMatrix1[(j + 2)] * paramColorMatrix2[14] + paramColorMatrix1[(j + 3)] * paramColorMatrix2[19] + paramColorMatrix1[(j + 4)]);
      j += 5;
      i = k;
    }
    if (arrayOfFloat != mArray) {
      System.arraycopy(arrayOfFloat, 0, mArray, 0, 20);
    }
  }
  
  public void setRGB2YUV()
  {
    reset();
    float[] arrayOfFloat = mArray;
    arrayOfFloat[0] = 0.299F;
    arrayOfFloat[1] = 0.587F;
    arrayOfFloat[2] = 0.114F;
    arrayOfFloat[5] = -0.16874F;
    arrayOfFloat[6] = -0.33126F;
    arrayOfFloat[7] = 0.5F;
    arrayOfFloat[10] = 0.5F;
    arrayOfFloat[11] = -0.41869F;
    arrayOfFloat[12] = -0.08131F;
  }
  
  public void setRotate(int paramInt, float paramFloat)
  {
    reset();
    double d = paramFloat * 3.141592653589793D / 180.0D;
    float f = (float)Math.cos(d);
    paramFloat = (float)Math.sin(d);
    float[] arrayOfFloat;
    switch (paramInt)
    {
    default: 
      throw new RuntimeException();
    case 2: 
      arrayOfFloat = mArray;
      mArray[6] = f;
      arrayOfFloat[0] = f;
      mArray[1] = paramFloat;
      mArray[5] = (-paramFloat);
      break;
    case 1: 
      arrayOfFloat = mArray;
      mArray[12] = f;
      arrayOfFloat[0] = f;
      mArray[2] = (-paramFloat);
      mArray[10] = paramFloat;
      break;
    case 0: 
      arrayOfFloat = mArray;
      mArray[12] = f;
      arrayOfFloat[6] = f;
      mArray[7] = paramFloat;
      mArray[11] = (-paramFloat);
    }
  }
  
  public void setSaturation(float paramFloat)
  {
    reset();
    float[] arrayOfFloat = mArray;
    float f1 = 1.0F - paramFloat;
    float f2 = 0.213F * f1;
    float f3 = 0.715F * f1;
    f1 = 0.072F * f1;
    arrayOfFloat[0] = (f2 + paramFloat);
    arrayOfFloat[1] = f3;
    arrayOfFloat[2] = f1;
    arrayOfFloat[5] = f2;
    arrayOfFloat[6] = (f3 + paramFloat);
    arrayOfFloat[7] = f1;
    arrayOfFloat[10] = f2;
    arrayOfFloat[11] = f3;
    arrayOfFloat[12] = (f1 + paramFloat);
  }
  
  public void setScale(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    float[] arrayOfFloat = mArray;
    for (int i = 19; i > 0; i--) {
      arrayOfFloat[i] = 0.0F;
    }
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[6] = paramFloat2;
    arrayOfFloat[12] = paramFloat3;
    arrayOfFloat[18] = paramFloat4;
  }
  
  public void setYUV2RGB()
  {
    reset();
    float[] arrayOfFloat = mArray;
    arrayOfFloat[2] = 1.402F;
    arrayOfFloat[5] = 1.0F;
    arrayOfFloat[6] = -0.34414F;
    arrayOfFloat[7] = -0.71414F;
    arrayOfFloat[10] = 1.0F;
    arrayOfFloat[11] = 1.772F;
    arrayOfFloat[12] = 0.0F;
  }
}
