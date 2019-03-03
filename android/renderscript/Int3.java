package android.renderscript;

public class Int3
{
  public int x;
  public int y;
  public int z;
  
  public Int3() {}
  
  public Int3(int paramInt)
  {
    z = paramInt;
    y = paramInt;
    x = paramInt;
  }
  
  public Int3(int paramInt1, int paramInt2, int paramInt3)
  {
    x = paramInt1;
    y = paramInt2;
    z = paramInt3;
  }
  
  public Int3(Int3 paramInt3)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public static Int3 add(Int3 paramInt3, int paramInt)
  {
    Int3 localInt3 = new Int3();
    x += paramInt;
    y += paramInt;
    z += paramInt;
    return localInt3;
  }
  
  public static Int3 add(Int3 paramInt31, Int3 paramInt32)
  {
    Int3 localInt3 = new Int3();
    x += x;
    y += y;
    z += z;
    return localInt3;
  }
  
  public static Int3 div(Int3 paramInt3, int paramInt)
  {
    Int3 localInt3 = new Int3();
    x /= paramInt;
    y /= paramInt;
    z /= paramInt;
    return localInt3;
  }
  
  public static Int3 div(Int3 paramInt31, Int3 paramInt32)
  {
    Int3 localInt3 = new Int3();
    x /= x;
    y /= y;
    z /= z;
    return localInt3;
  }
  
  public static int dotProduct(Int3 paramInt31, Int3 paramInt32)
  {
    return x * x + y * y + z * z;
  }
  
  public static Int3 mod(Int3 paramInt3, int paramInt)
  {
    Int3 localInt3 = new Int3();
    x %= paramInt;
    y %= paramInt;
    z %= paramInt;
    return localInt3;
  }
  
  public static Int3 mod(Int3 paramInt31, Int3 paramInt32)
  {
    Int3 localInt3 = new Int3();
    x %= x;
    y %= y;
    z %= z;
    return localInt3;
  }
  
  public static Int3 mul(Int3 paramInt3, int paramInt)
  {
    Int3 localInt3 = new Int3();
    x *= paramInt;
    y *= paramInt;
    z *= paramInt;
    return localInt3;
  }
  
  public static Int3 mul(Int3 paramInt31, Int3 paramInt32)
  {
    Int3 localInt3 = new Int3();
    x *= x;
    y *= y;
    z *= z;
    return localInt3;
  }
  
  public static Int3 sub(Int3 paramInt3, int paramInt)
  {
    Int3 localInt3 = new Int3();
    x -= paramInt;
    y -= paramInt;
    z -= paramInt;
    return localInt3;
  }
  
  public static Int3 sub(Int3 paramInt31, Int3 paramInt32)
  {
    Int3 localInt3 = new Int3();
    x -= x;
    y -= y;
    z -= z;
    return localInt3;
  }
  
  public void add(int paramInt)
  {
    x += paramInt;
    y += paramInt;
    z += paramInt;
  }
  
  public void add(Int3 paramInt3)
  {
    x += x;
    y += y;
    z += z;
  }
  
  public void addAt(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z += paramInt2;
      return;
    case 1: 
      y += paramInt2;
      return;
    }
    x += paramInt2;
  }
  
  public void addMultiple(Int3 paramInt3, int paramInt)
  {
    x += x * paramInt;
    y += y * paramInt;
    z += z * paramInt;
  }
  
  public void copyTo(int[] paramArrayOfInt, int paramInt)
  {
    paramArrayOfInt[paramInt] = x;
    paramArrayOfInt[(paramInt + 1)] = y;
    paramArrayOfInt[(paramInt + 2)] = z;
  }
  
  public void div(int paramInt)
  {
    x /= paramInt;
    y /= paramInt;
    z /= paramInt;
  }
  
  public void div(Int3 paramInt3)
  {
    x /= x;
    y /= y;
    z /= z;
  }
  
  public int dotProduct(Int3 paramInt3)
  {
    return x * x + y * y + z * z;
  }
  
  public int elementSum()
  {
    return x + y + z;
  }
  
  public int get(int paramInt)
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
  
  public void mod(int paramInt)
  {
    x %= paramInt;
    y %= paramInt;
    z %= paramInt;
  }
  
  public void mod(Int3 paramInt3)
  {
    x %= x;
    y %= y;
    z %= z;
  }
  
  public void mul(int paramInt)
  {
    x *= paramInt;
    y *= paramInt;
    z *= paramInt;
  }
  
  public void mul(Int3 paramInt3)
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
  
  public void set(Int3 paramInt3)
  {
    x = x;
    y = y;
    z = z;
  }
  
  public void setAt(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 2: 
      z = paramInt2;
      return;
    case 1: 
      y = paramInt2;
      return;
    }
    x = paramInt2;
  }
  
  public void setValues(int paramInt1, int paramInt2, int paramInt3)
  {
    x = paramInt1;
    y = paramInt2;
    z = paramInt3;
  }
  
  public void sub(int paramInt)
  {
    x -= paramInt;
    y -= paramInt;
    z -= paramInt;
  }
  
  public void sub(Int3 paramInt3)
  {
    x -= x;
    y -= y;
    z -= z;
  }
}
