package android.renderscript;

public class Int2
{
  public int x;
  public int y;
  
  public Int2() {}
  
  public Int2(int paramInt)
  {
    y = paramInt;
    x = paramInt;
  }
  
  public Int2(int paramInt1, int paramInt2)
  {
    x = paramInt1;
    y = paramInt2;
  }
  
  public Int2(Int2 paramInt2)
  {
    x = x;
    y = y;
  }
  
  public static Int2 add(Int2 paramInt2, int paramInt)
  {
    Int2 localInt2 = new Int2();
    x += paramInt;
    y += paramInt;
    return localInt2;
  }
  
  public static Int2 add(Int2 paramInt21, Int2 paramInt22)
  {
    Int2 localInt2 = new Int2();
    x += x;
    y += y;
    return localInt2;
  }
  
  public static Int2 div(Int2 paramInt2, int paramInt)
  {
    Int2 localInt2 = new Int2();
    x /= paramInt;
    y /= paramInt;
    return localInt2;
  }
  
  public static Int2 div(Int2 paramInt21, Int2 paramInt22)
  {
    Int2 localInt2 = new Int2();
    x /= x;
    y /= y;
    return localInt2;
  }
  
  public static int dotProduct(Int2 paramInt21, Int2 paramInt22)
  {
    return x * x + y * y;
  }
  
  public static Int2 mod(Int2 paramInt2, int paramInt)
  {
    Int2 localInt2 = new Int2();
    x %= paramInt;
    y %= paramInt;
    return localInt2;
  }
  
  public static Int2 mod(Int2 paramInt21, Int2 paramInt22)
  {
    Int2 localInt2 = new Int2();
    x %= x;
    y %= y;
    return localInt2;
  }
  
  public static Int2 mul(Int2 paramInt2, int paramInt)
  {
    Int2 localInt2 = new Int2();
    x *= paramInt;
    y *= paramInt;
    return localInt2;
  }
  
  public static Int2 mul(Int2 paramInt21, Int2 paramInt22)
  {
    Int2 localInt2 = new Int2();
    x *= x;
    y *= y;
    return localInt2;
  }
  
  public static Int2 sub(Int2 paramInt2, int paramInt)
  {
    Int2 localInt2 = new Int2();
    x -= paramInt;
    y -= paramInt;
    return localInt2;
  }
  
  public static Int2 sub(Int2 paramInt21, Int2 paramInt22)
  {
    Int2 localInt2 = new Int2();
    x -= x;
    y -= y;
    return localInt2;
  }
  
  public void add(int paramInt)
  {
    x += paramInt;
    y += paramInt;
  }
  
  public void add(Int2 paramInt2)
  {
    x += x;
    y += y;
  }
  
  public void addAt(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y += paramInt2;
      return;
    }
    x += paramInt2;
  }
  
  public void addMultiple(Int2 paramInt2, int paramInt)
  {
    x += x * paramInt;
    y += y * paramInt;
  }
  
  public void copyTo(int[] paramArrayOfInt, int paramInt)
  {
    paramArrayOfInt[paramInt] = x;
    paramArrayOfInt[(paramInt + 1)] = y;
  }
  
  public void div(int paramInt)
  {
    x /= paramInt;
    y /= paramInt;
  }
  
  public void div(Int2 paramInt2)
  {
    x /= x;
    y /= y;
  }
  
  public int dotProduct(Int2 paramInt2)
  {
    return x * x + y * y;
  }
  
  public int elementSum()
  {
    return x + y;
  }
  
  public int get(int paramInt)
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
  
  public void mod(int paramInt)
  {
    x %= paramInt;
    y %= paramInt;
  }
  
  public void mod(Int2 paramInt2)
  {
    x %= x;
    y %= y;
  }
  
  public void mul(int paramInt)
  {
    x *= paramInt;
    y *= paramInt;
  }
  
  public void mul(Int2 paramInt2)
  {
    x *= x;
    y *= y;
  }
  
  public void negate()
  {
    x = (-x);
    y = (-y);
  }
  
  public void set(Int2 paramInt2)
  {
    x = x;
    y = y;
  }
  
  public void setAt(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y = paramInt2;
      return;
    }
    x = paramInt2;
  }
  
  public void setValues(int paramInt1, int paramInt2)
  {
    x = paramInt1;
    y = paramInt2;
  }
  
  public void sub(int paramInt)
  {
    x -= paramInt;
    y -= paramInt;
  }
  
  public void sub(Int2 paramInt2)
  {
    x -= x;
    y -= y;
  }
}
