package android.renderscript;

public class Float2
{
  public float x;
  public float y;
  
  public Float2() {}
  
  public Float2(float paramFloat1, float paramFloat2)
  {
    x = paramFloat1;
    y = paramFloat2;
  }
  
  public Float2(Float2 paramFloat2)
  {
    x = x;
    y = y;
  }
  
  public static Float2 add(Float2 paramFloat2, float paramFloat)
  {
    Float2 localFloat2 = new Float2();
    x += paramFloat;
    y += paramFloat;
    return localFloat2;
  }
  
  public static Float2 add(Float2 paramFloat21, Float2 paramFloat22)
  {
    Float2 localFloat2 = new Float2();
    x += x;
    y += y;
    return localFloat2;
  }
  
  public static Float2 div(Float2 paramFloat2, float paramFloat)
  {
    Float2 localFloat2 = new Float2();
    x /= paramFloat;
    y /= paramFloat;
    return localFloat2;
  }
  
  public static Float2 div(Float2 paramFloat21, Float2 paramFloat22)
  {
    Float2 localFloat2 = new Float2();
    x /= x;
    y /= y;
    return localFloat2;
  }
  
  public static float dotProduct(Float2 paramFloat21, Float2 paramFloat22)
  {
    return x * x + y * y;
  }
  
  public static Float2 mul(Float2 paramFloat2, float paramFloat)
  {
    Float2 localFloat2 = new Float2();
    x *= paramFloat;
    y *= paramFloat;
    return localFloat2;
  }
  
  public static Float2 mul(Float2 paramFloat21, Float2 paramFloat22)
  {
    Float2 localFloat2 = new Float2();
    x *= x;
    y *= y;
    return localFloat2;
  }
  
  public static Float2 sub(Float2 paramFloat2, float paramFloat)
  {
    Float2 localFloat2 = new Float2();
    x -= paramFloat;
    y -= paramFloat;
    return localFloat2;
  }
  
  public static Float2 sub(Float2 paramFloat21, Float2 paramFloat22)
  {
    Float2 localFloat2 = new Float2();
    x -= x;
    y -= y;
    return localFloat2;
  }
  
  public void add(float paramFloat)
  {
    x += paramFloat;
    y += paramFloat;
  }
  
  public void add(Float2 paramFloat2)
  {
    x += x;
    y += y;
  }
  
  public void addAt(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y += paramFloat;
      return;
    }
    x += paramFloat;
  }
  
  public void addMultiple(Float2 paramFloat2, float paramFloat)
  {
    x += x * paramFloat;
    y += y * paramFloat;
  }
  
  public void copyTo(float[] paramArrayOfFloat, int paramInt)
  {
    paramArrayOfFloat[paramInt] = x;
    paramArrayOfFloat[(paramInt + 1)] = y;
  }
  
  public void div(float paramFloat)
  {
    x /= paramFloat;
    y /= paramFloat;
  }
  
  public void div(Float2 paramFloat2)
  {
    x /= x;
    y /= y;
  }
  
  public float dotProduct(Float2 paramFloat2)
  {
    return x * x + y * y;
  }
  
  public float elementSum()
  {
    return x + y;
  }
  
  public float get(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      return y;
    }
    return x;
  }
  
  public int length()
  {
    return 2;
  }
  
  public void mul(float paramFloat)
  {
    x *= paramFloat;
    y *= paramFloat;
  }
  
  public void mul(Float2 paramFloat2)
  {
    x *= x;
    y *= y;
  }
  
  public void negate()
  {
    x = (-x);
    y = (-y);
  }
  
  public void set(Float2 paramFloat2)
  {
    x = x;
    y = y;
  }
  
  public void setAt(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y = paramFloat;
      return;
    }
    x = paramFloat;
  }
  
  public void setValues(float paramFloat1, float paramFloat2)
  {
    x = paramFloat1;
    y = paramFloat2;
  }
  
  public void sub(float paramFloat)
  {
    x -= paramFloat;
    y -= paramFloat;
  }
  
  public void sub(Float2 paramFloat2)
  {
    x -= x;
    y -= y;
  }
}
