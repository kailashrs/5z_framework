package android.renderscript;

public class Int4
{
  public int w;
  public int x;
  public int y;
  public int z;
  
  public Int4() {}
  
  public Int4(int paramInt)
  {
    w = paramInt;
    z = paramInt;
    y = paramInt;
    x = paramInt;
  }
  
  public Int4(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    x = paramInt1;
    y = paramInt2;
    z = paramInt3;
    w = paramInt4;
  }
  
  public Int4(Int4 paramInt4)
  {
    x = x;
    y = y;
    z = z;
    w = w;
  }
  
  public static Int4 add(Int4 paramInt4, int paramInt)
  {
    Int4 localInt4 = new Int4();
    x += paramInt;
    y += paramInt;
    z += paramInt;
    w += paramInt;
    return localInt4;
  }
  
  public static Int4 add(Int4 paramInt41, Int4 paramInt42)
  {
    Int4 localInt4 = new Int4();
    x += x;
    y += y;
    z += z;
    w += w;
    return localInt4;
  }
  
  public static Int4 div(Int4 paramInt4, int paramInt)
  {
    Int4 localInt4 = new Int4();
    x /= paramInt;
    y /= paramInt;
    z /= paramInt;
    w /= paramInt;
    return localInt4;
  }
  
  public static Int4 div(Int4 paramInt41, Int4 paramInt42)
  {
    Int4 localInt4 = new Int4();
    x /= x;
    y /= y;
    z /= z;
    w /= w;
    return localInt4;
  }
  
  public static int dotProduct(Int4 paramInt41, Int4 paramInt42)
  {
    return x * x + y * y + z * z + w * w;
  }
  
  public static Int4 mod(Int4 paramInt4, int paramInt)
  {
    Int4 localInt4 = new Int4();
    x %= paramInt;
    y %= paramInt;
    z %= paramInt;
    w %= paramInt;
    return localInt4;
  }
  
  public static Int4 mod(Int4 paramInt41, Int4 paramInt42)
  {
    Int4 localInt4 = new Int4();
    x %= x;
    y %= y;
    z %= z;
    w %= w;
    return localInt4;
  }
  
  public static Int4 mul(Int4 paramInt4, int paramInt)
  {
    Int4 localInt4 = new Int4();
    x *= paramInt;
    y *= paramInt;
    z *= paramInt;
    w *= paramInt;
    return localInt4;
  }
  
  public static Int4 mul(Int4 paramInt41, Int4 paramInt42)
  {
    Int4 localInt4 = new Int4();
    x *= x;
    y *= y;
    z *= z;
    w *= w;
    return localInt4;
  }
  
  public static Int4 sub(Int4 paramInt4, int paramInt)
  {
    Int4 localInt4 = new Int4();
    x -= paramInt;
    y -= paramInt;
    z -= paramInt;
    w -= paramInt;
    return localInt4;
  }
  
  public static Int4 sub(Int4 paramInt41, Int4 paramInt42)
  {
    Int4 localInt4 = new Int4();
    x -= x;
    y -= y;
    z -= z;
    w -= w;
    return localInt4;
  }
  
  public void add(int paramInt)
  {
    x += paramInt;
    y += paramInt;
    z += paramInt;
    w += paramInt;
  }
  
  public void add(Int4 paramInt4)
  {
    x += x;
    y += y;
    z += z;
    w += w;
  }
  
  public void addAt(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w += paramInt2;
      return;
    case 2: 
      z += paramInt2;
      return;
    case 1: 
      y += paramInt2;
      return;
    }
    x += paramInt2;
  }
  
  public void addMultiple(Int4 paramInt4, int paramInt)
  {
    x += x * paramInt;
    y += y * paramInt;
    z += z * paramInt;
    w += w * paramInt;
  }
  
  public void copyTo(int[] paramArrayOfInt, int paramInt)
  {
    paramArrayOfInt[paramInt] = x;
    paramArrayOfInt[(paramInt + 1)] = y;
    paramArrayOfInt[(paramInt + 2)] = z;
    paramArrayOfInt[(paramInt + 3)] = w;
  }
  
  public void div(int paramInt)
  {
    x /= paramInt;
    y /= paramInt;
    z /= paramInt;
    w /= paramInt;
  }
  
  public void div(Int4 paramInt4)
  {
    x /= x;
    y /= y;
    z /= z;
    w /= w;
  }
  
  public int dotProduct(Int4 paramInt4)
  {
    return x * x + y * y + z * z + w * w;
  }
  
  public int elementSum()
  {
    return x + y + z + w;
  }
  
  public int get(int paramInt)
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
  
  public void mod(int paramInt)
  {
    x %= paramInt;
    y %= paramInt;
    z %= paramInt;
    w %= paramInt;
  }
  
  public void mod(Int4 paramInt4)
  {
    x %= x;
    y %= y;
    z %= z;
    w %= w;
  }
  
  public void mul(int paramInt)
  {
    x *= paramInt;
    y *= paramInt;
    z *= paramInt;
    w *= paramInt;
  }
  
  public void mul(Int4 paramInt4)
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
  
  public void set(Int4 paramInt4)
  {
    x = x;
    y = y;
    z = z;
    w = w;
  }
  
  public void setAt(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 3: 
      w = paramInt2;
      return;
    case 2: 
      z = paramInt2;
      return;
    case 1: 
      y = paramInt2;
      return;
    }
    x = paramInt2;
  }
  
  public void setValues(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    x = paramInt1;
    y = paramInt2;
    z = paramInt3;
    w = paramInt4;
  }
  
  public void sub(int paramInt)
  {
    x -= paramInt;
    y -= paramInt;
    z -= paramInt;
    w -= paramInt;
  }
  
  public void sub(Int4 paramInt4)
  {
    x -= x;
    y -= y;
    z -= z;
    w -= w;
  }
}
