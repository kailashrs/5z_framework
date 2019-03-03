package android.renderscript;

public class Float3
{
  public float x;
  public float y;
  public float z;
  
  public Float3() {}
  
  public Float3(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    x = paramFloat1;
    y = paramFloat2;
    z = paramFloat3;
  }
  
  public Float3(Float3 paramFloat3)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public static Float3 add(Float3 paramFloat3, float paramFloat)
  {
    Float3 localFloat3 = new Float3();
    x += paramFloat;
    y += paramFloat;
    z += paramFloat;
    return localFloat3;
  }
  
  public static Float3 add(Float3 paramFloat31, Float3 paramFloat32)
  {
    Float3 localFloat3 = new Float3();
    x += x;
    y += y;
    z += z;
    return localFloat3;
  }
  
  public static Float3 div(Float3 paramFloat3, float paramFloat)
  {
    Float3 localFloat3 = new Float3();
    x /= paramFloat;
    y /= paramFloat;
    z /= paramFloat;
    return localFloat3;
  }
  
  public static Float3 div(Float3 paramFloat31, Float3 paramFloat32)
  {
    Float3 localFloat3 = new Float3();
    x /= x;
    y /= y;
    z /= z;
    return localFloat3;
  }
  
  public static Float dotProduct(Float3 paramFloat31, Float3 paramFloat32)
  {
    return new Float(x * x + y * y + z * z);
  }
  
  public static Float3 mul(Float3 paramFloat3, float paramFloat)
  {
    Float3 localFloat3 = new Float3();
    x *= paramFloat;
    y *= paramFloat;
    z *= paramFloat;
    return localFloat3;
  }
  
  public static Float3 mul(Float3 paramFloat31, Float3 paramFloat32)
  {
    Float3 localFloat3 = new Float3();
    x *= x;
    y *= y;
    z *= z;
    return localFloat3;
  }
  
  public static Float3 sub(Float3 paramFloat3, float paramFloat)
  {
    Float3 localFloat3 = new Float3();
    x -= paramFloat;
    y -= paramFloat;
    z -= paramFloat;
    return localFloat3;
  }
  
  public static Float3 sub(Float3 paramFloat31, Float3 paramFloat32)
  {
    Float3 localFloat3 = new Float3();
    x -= x;
    y -= y;
    z -= z;
    return localFloat3;
  }
  
  public void add(float paramFloat)
  {
    x += paramFloat;
    y += paramFloat;
    z += paramFloat;
  }
  
  public void add(Float3 paramFloat3)
  {
    x += x;
    y += y;
    z += z;
  }
  
  public void addAt(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z += paramFloat;
      return;
    case 1: 
      y += paramFloat;
      return;
    }
    x += paramFloat;
  }
  
  public void addMultiple(Float3 paramFloat3, float paramFloat)
  {
    x += x * paramFloat;
    y += y * paramFloat;
    z += z * paramFloat;
  }
  
  public void copyTo(float[] paramArrayOfFloat, int paramInt)
  {
    paramArrayOfFloat[paramInt] = x;
    paramArrayOfFloat[(paramInt + 1)] = y;
    paramArrayOfFloat[(paramInt + 2)] = z;
  }
  
  public void div(float paramFloat)
  {
    x /= paramFloat;
    y /= paramFloat;
    z /= paramFloat;
  }
  
  public void div(Float3 paramFloat3)
  {
    x /= x;
    y /= y;
    z /= z;
  }
  
  public Float dotProduct(Float3 paramFloat3)
  {
    return new Float(x * x + y * y + z * z);
  }
  
  public Float elementSum()
  {
    return new Float(x + y + z);
  }
  
  public float get(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      return z;
    case 1: 
      return y;
    }
    return x;
  }
  
  public int length()
  {
    return 3;
  }
  
  public void mul(float paramFloat)
  {
    x *= paramFloat;
    y *= paramFloat;
    z *= paramFloat;
  }
  
  public void mul(Float3 paramFloat3)
  {
    x *= x;
    y *= y;
    z *= z;
  }
  
  public void negate()
  {
    x = (-x);
    y = (-y);
    z = (-z);
  }
  
  public void set(Float3 paramFloat3)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public void setAt(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z = paramFloat;
      return;
    case 1: 
      y = paramFloat;
      return;
    }
    x = paramFloat;
  }
  
  public void setValues(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    x = paramFloat1;
    y = paramFloat2;
    z = paramFloat3;
  }
  
  public void sub(float paramFloat)
  {
    x -= paramFloat;
    y -= paramFloat;
    z -= paramFloat;
  }
  
  public void sub(Float3 paramFloat3)
  {
    x -= x;
    y -= y;
    z -= z;
  }
}
