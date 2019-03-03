package android.renderscript;

public class Float4
{
  public float w;
  public float x;
  public float y;
  public float z;
  
  public Float4() {}
  
  public Float4(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    x = paramFloat1;
    y = paramFloat2;
    z = paramFloat3;
    w = paramFloat4;
  }
  
  public Float4(Float4 paramFloat4)
  {
    x = x;
    y = y;
    z = z;
    w = w;
  }
  
  public static Float4 add(Float4 paramFloat4, float paramFloat)
  {
    Float4 localFloat4 = new Float4();
    x += paramFloat;
    y += paramFloat;
    z += paramFloat;
    w += paramFloat;
    return localFloat4;
  }
  
  public static Float4 add(Float4 paramFloat41, Float4 paramFloat42)
  {
    Float4 localFloat4 = new Float4();
    x += x;
    y += y;
    z += z;
    w += w;
    return localFloat4;
  }
  
  public static Float4 div(Float4 paramFloat4, float paramFloat)
  {
    Float4 localFloat4 = new Float4();
    x /= paramFloat;
    y /= paramFloat;
    z /= paramFloat;
    w /= paramFloat;
    return localFloat4;
  }
  
  public static Float4 div(Float4 paramFloat41, Float4 paramFloat42)
  {
    Float4 localFloat4 = new Float4();
    x /= x;
    y /= y;
    z /= z;
    w /= w;
    return localFloat4;
  }
  
  public static float dotProduct(Float4 paramFloat41, Float4 paramFloat42)
  {
    return x * x + y * y + z * z + w * w;
  }
  
  public static Float4 mul(Float4 paramFloat4, float paramFloat)
  {
    Float4 localFloat4 = new Float4();
    x *= paramFloat;
    y *= paramFloat;
    z *= paramFloat;
    w *= paramFloat;
    return localFloat4;
  }
  
  public static Float4 mul(Float4 paramFloat41, Float4 paramFloat42)
  {
    Float4 localFloat4 = new Float4();
    x *= x;
    y *= y;
    z *= z;
    w *= w;
    return localFloat4;
  }
  
  public static Float4 sub(Float4 paramFloat4, float paramFloat)
  {
    Float4 localFloat4 = new Float4();
    x -= paramFloat;
    y -= paramFloat;
    z -= paramFloat;
    w -= paramFloat;
    return localFloat4;
  }
  
  public static Float4 sub(Float4 paramFloat41, Float4 paramFloat42)
  {
    Float4 localFloat4 = new Float4();
    x -= x;
    y -= y;
    z -= z;
    w -= w;
    return localFloat4;
  }
  
  public void add(float paramFloat)
  {
    x += paramFloat;
    y += paramFloat;
    z += paramFloat;
    w += paramFloat;
  }
  
  public void add(Float4 paramFloat4)
  {
    x += x;
    y += y;
    z += z;
    w += w;
  }
  
  public void addAt(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w += paramFloat;
      return;
    case 2: 
      z += paramFloat;
      return;
    case 1: 
      y += paramFloat;
      return;
    }
    x += paramFloat;
  }
  
  public void addMultiple(Float4 paramFloat4, float paramFloat)
  {
    x += x * paramFloat;
    y += y * paramFloat;
    z += z * paramFloat;
    w += w * paramFloat;
  }
  
  public void copyTo(float[] paramArrayOfFloat, int paramInt)
  {
    paramArrayOfFloat[paramInt] = x;
    paramArrayOfFloat[(paramInt + 1)] = y;
    paramArrayOfFloat[(paramInt + 2)] = z;
    paramArrayOfFloat[(paramInt + 3)] = w;
  }
  
  public void div(float paramFloat)
  {
    x /= paramFloat;
    y /= paramFloat;
    z /= paramFloat;
    w /= paramFloat;
  }
  
  public void div(Float4 paramFloat4)
  {
    x /= x;
    y /= y;
    z /= z;
    w /= w;
  }
  
  public float dotProduct(Float4 paramFloat4)
  {
    return x * x + y * y + z * z + w * w;
  }
  
  public float elementSum()
  {
    return x + y + z + w;
  }
  
  public float get(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      return w;
    case 2: 
      return z;
    case 1: 
      return y;
    }
    return x;
  }
  
  public int length()
  {
    return 4;
  }
  
  public void mul(float paramFloat)
  {
    x *= paramFloat;
    y *= paramFloat;
    z *= paramFloat;
    w *= paramFloat;
  }
  
  public void mul(Float4 paramFloat4)
  {
    x *= x;
    y *= y;
    z *= z;
    w *= w;
  }
  
  public void negate()
  {
    x = (-x);
    y = (-y);
    z = (-z);
    w = (-w);
  }
  
  public void set(Float4 paramFloat4)
  {
    x = x;
    y = y;
    z = z;
    w = w;
  }
  
  public void setAt(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w = paramFloat;
      return;
    case 2: 
      z = paramFloat;
      return;
    case 1: 
      y = paramFloat;
      return;
    }
    x = paramFloat;
  }
  
  public void setValues(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    x = paramFloat1;
    y = paramFloat2;
    z = paramFloat3;
    w = paramFloat4;
  }
  
  public void sub(float paramFloat)
  {
    x -= paramFloat;
    y -= paramFloat;
    z -= paramFloat;
    w -= paramFloat;
  }
  
  public void sub(Float4 paramFloat4)
  {
    x -= x;
    y -= y;
    z -= z;
    w -= w;
  }
}
