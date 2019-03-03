package android.renderscript;

public class Short2
{
  public short x;
  public short y;
  
  public Short2() {}
  
  public Short2(Short2 paramShort2)
  {
    x = ((short)x);
    y = ((short)y);
  }
  
  public Short2(short paramShort)
  {
    y = ((short)paramShort);
    x = ((short)paramShort);
  }
  
  public Short2(short paramShort1, short paramShort2)
  {
    x = ((short)paramShort1);
    y = ((short)paramShort2);
  }
  
  public static Short2 add(Short2 paramShort21, Short2 paramShort22)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x + x));
    y = ((short)(short)(y + y));
    return localShort2;
  }
  
  public static Short2 add(Short2 paramShort2, short paramShort)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x + paramShort));
    y = ((short)(short)(y + paramShort));
    return localShort2;
  }
  
  public static Short2 div(Short2 paramShort21, Short2 paramShort22)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x / x));
    y = ((short)(short)(y / y));
    return localShort2;
  }
  
  public static Short2 div(Short2 paramShort2, short paramShort)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x / paramShort));
    y = ((short)(short)(y / paramShort));
    return localShort2;
  }
  
  public static short dotProduct(Short2 paramShort21, Short2 paramShort22)
  {
    return (short)(x * x + y * y);
  }
  
  public static Short2 mod(Short2 paramShort21, Short2 paramShort22)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x % x));
    y = ((short)(short)(y % y));
    return localShort2;
  }
  
  public static Short2 mod(Short2 paramShort2, short paramShort)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x % paramShort));
    y = ((short)(short)(y % paramShort));
    return localShort2;
  }
  
  public static Short2 mul(Short2 paramShort21, Short2 paramShort22)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x * x));
    y = ((short)(short)(y * y));
    return localShort2;
  }
  
  public static Short2 mul(Short2 paramShort2, short paramShort)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x * paramShort));
    y = ((short)(short)(y * paramShort));
    return localShort2;
  }
  
  public static Short2 sub(Short2 paramShort21, Short2 paramShort22)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x - x));
    y = ((short)(short)(y - y));
    return localShort2;
  }
  
  public static Short2 sub(Short2 paramShort2, short paramShort)
  {
    Short2 localShort2 = new Short2();
    x = ((short)(short)(x - paramShort));
    y = ((short)(short)(y - paramShort));
    return localShort2;
  }
  
  public void add(Short2 paramShort2)
  {
    x = ((short)(short)(x + x));
    y = ((short)(short)(y + y));
  }
  
  public void add(short paramShort)
  {
    x = ((short)(short)(x + paramShort));
    y = ((short)(short)(y + paramShort));
  }
  
  public void addAt(int paramInt, short paramShort)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y = ((short)(short)(y + paramShort));
      return;
    }
    x = ((short)(short)(x + paramShort));
  }
  
  public void addMultiple(Short2 paramShort2, short paramShort)
  {
    x = ((short)(short)(x + x * paramShort));
    y = ((short)(short)(y + y * paramShort));
  }
  
  public void copyTo(short[] paramArrayOfShort, int paramInt)
  {
    paramArrayOfShort[paramInt] = ((short)x);
    paramArrayOfShort[(paramInt + 1)] = ((short)y);
  }
  
  public void div(Short2 paramShort2)
  {
    x = ((short)(short)(x / x));
    y = ((short)(short)(y / y));
  }
  
  public void div(short paramShort)
  {
    x = ((short)(short)(x / paramShort));
    y = ((short)(short)(y / paramShort));
  }
  
  public short dotProduct(Short2 paramShort2)
  {
    return (short)(x * x + y * y);
  }
  
  public short elementSum()
  {
    return (short)(x + y);
  }
  
  public short get(int paramInt)
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
  
  public short length()
  {
    return 2;
  }
  
  public void mod(Short2 paramShort2)
  {
    x = ((short)(short)(x % x));
    y = ((short)(short)(y % y));
  }
  
  public void mod(short paramShort)
  {
    x = ((short)(short)(x % paramShort));
    y = ((short)(short)(y % paramShort));
  }
  
  public void mul(Short2 paramShort2)
  {
    x = ((short)(short)(x * x));
    y = ((short)(short)(y * y));
  }
  
  public void mul(short paramShort)
  {
    x = ((short)(short)(x * paramShort));
    y = ((short)(short)(y * paramShort));
  }
  
  public void negate()
  {
    x = ((short)(short)-x);
    y = ((short)(short)-y);
  }
  
  public void set(Short2 paramShort2)
  {
    x = ((short)x);
    y = ((short)y);
  }
  
  public void setAt(int paramInt, short paramShort)
  {
    switch (paramInt)
    {
    default: 
      throw new IndexOutOfBoundsException("Index: i");
    case 1: 
      y = ((short)paramShort);
      return;
    }
    x = ((short)paramShort);
  }
  
  public void setValues(short paramShort1, short paramShort2)
  {
    x = ((short)paramShort1);
    y = ((short)paramShort2);
  }
  
  public void sub(Short2 paramShort2)
  {
    x = ((short)(short)(x - x));
    y = ((short)(short)(y - y));
  }
  
  public void sub(short paramShort)
  {
    x = ((short)(short)(x - paramShort));
    y = ((short)(short)(y - paramShort));
  }
}
